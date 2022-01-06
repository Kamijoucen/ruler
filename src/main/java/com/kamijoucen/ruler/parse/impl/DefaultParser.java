package com.kamijoucen.ruler.parse.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.OperationNode;
import com.kamijoucen.ruler.ast.expression.AssignNode;
import com.kamijoucen.ruler.ast.expression.BlockNode;
import com.kamijoucen.ruler.ast.expression.CallLinkNode;
import com.kamijoucen.ruler.ast.expression.CallNode;
import com.kamijoucen.ruler.ast.expression.ClosureDefineNode;
import com.kamijoucen.ruler.ast.expression.DotNode;
import com.kamijoucen.ruler.ast.expression.IfStatementNode;
import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.ast.expression.IndexNode;
import com.kamijoucen.ruler.ast.expression.LoopBlockNode;
import com.kamijoucen.ruler.ast.expression.VariableDefineNode;
import com.kamijoucen.ruler.ast.expression.WhileStatementNode;
import com.kamijoucen.ruler.ast.facotr.ArrayNode;
import com.kamijoucen.ruler.ast.facotr.BinaryOperationNode;
import com.kamijoucen.ruler.ast.facotr.BoolNode;
import com.kamijoucen.ruler.ast.facotr.BreakNode;
import com.kamijoucen.ruler.ast.facotr.ContinueNode;
import com.kamijoucen.ruler.ast.facotr.DoubleNode;
import com.kamijoucen.ruler.ast.facotr.IntegerNode;
import com.kamijoucen.ruler.ast.facotr.LogicBinaryOperationNode;
import com.kamijoucen.ruler.ast.facotr.NullNode;
import com.kamijoucen.ruler.ast.facotr.OutNameNode;
import com.kamijoucen.ruler.ast.facotr.ReturnNode;
import com.kamijoucen.ruler.ast.facotr.RsonNode;
import com.kamijoucen.ruler.ast.facotr.StringNode;
import com.kamijoucen.ruler.ast.facotr.ThisNode;
import com.kamijoucen.ruler.ast.facotr.TypeOfNode;
import com.kamijoucen.ruler.ast.facotr.UnaryOperationNode;
import com.kamijoucen.ruler.common.RStack;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.operation.UnaryAddOperation;
import com.kamijoucen.ruler.operation.UnarySubOperation;
import com.kamijoucen.ruler.parse.Parser;
import com.kamijoucen.ruler.parse.TokenStream;
import com.kamijoucen.ruler.common.OperationDefine;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.typecheck.TypeCheckVisitor;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.util.SyntaxCheckUtil;
import com.kamijoucen.ruler.util.TokenUtil;

public class DefaultParser implements Parser {

    private ParseContext parseContext;
    private RuntimeContext runtimeContext;
    private final List<BaseNode> statements;
    private final TokenStream tokenStream;

    public DefaultParser(TokenStream tokenStream) {
        initContext();
        this.tokenStream = tokenStream;
        this.statements = new ArrayList<BaseNode>();
    }

    private void initContext() {
        TypeCheckVisitor typeCheckVisitor = new TypeCheckVisitor();
        this.parseContext = new ParseContext(typeCheckVisitor);
        this.runtimeContext = new RuntimeContext(null, null, typeCheckVisitor);
    }

    @Override
    public List<BaseNode> parse() {
        tokenStream.nextToken();
        List<ImportNode> imports = null;
        if (tokenStream.token().type == TokenType.KEY_IMPORT) {
            imports = parseImports();
        }
        if (!CollectionUtil.isEmpty(imports)) {
            SyntaxCheckUtil.availableImport(imports);
            statements.addAll(imports);
        }
        while (tokenStream.token().type != TokenType.EOF) {
            statements.add(parseStatement());
        }
        return statements;
    }

    public List<ImportNode> parseImports() {
        if (tokenStream.token().type != TokenType.KEY_IMPORT) {
            return Collections.emptyList();
        }
        List<ImportNode> imports = new ArrayList<ImportNode>();
        while (tokenStream.token().type == TokenType.KEY_IMPORT) {
            BaseNode importNode = parseImport();
            statements.add(importNode);
            imports.add((ImportNode) importNode);
        }
        return imports;
    }

    public BaseNode parseStatement() {
        Token token = tokenStream.token();
        BaseNode statement = null;
        boolean isNeedSemicolon = false;
        switch (token.type) {
            case IDENTIFIER:
            case OUT_IDENTIFIER:
            case LEFT_PAREN:
            case KEY_THIS:
                statement = parseCallLink(true);
                isNeedSemicolon = true;
                break;
            case KEY_RETURN:
                statement = parseReturn();
                isNeedSemicolon = true;
                break;
            case KEY_DEF:
                break;
            case KEY_IF:
                statement = parseIfStatement();
                break;
            case KEY_FOR:
                break;
            case KEY_WHILE:
                statement = parseWhileStatement();
                break;
            case KEY_BREAK:
                statement = parseBreak();
                isNeedSemicolon = true;
                break;
            case KEY_CONTINUE:
                statement = parseContinue();
                isNeedSemicolon = true;
                break;
            case KEY_FUN:
                statement = parseFunDefine();
                break;
            case KEY_VAR:
                statement = parseVariableDefine();
                isNeedSemicolon = true;
                break;
        }
        if (statement == null) {
            throw SyntaxException.withSyntax("错误的语句");
        }
        if (isNeedSemicolon) {
            AssertUtil.assertToken(tokenStream, TokenType.SEMICOLON);
            tokenStream.nextToken();
        }
        return statement;
    }

    public BaseNode parseCallLinkAssignNode(BaseNode callLinkNode) {
        AssertUtil.assertToken(tokenStream.token(), TokenType.ASSIGN);
        tokenStream.nextToken();
        BaseNode expression = parseExpression();
        return new AssignNode(callLinkNode, expression);
    }

    public BaseNode parseExpression() {

        RStack<BaseNode> valStack = new RStack<BaseNode>();
        RStack<TokenType> opStack = new RStack<TokenType>();

        valStack.push(parsePrimaryExpression()); // first exp
        while (true) {
            Token op = tokenStream.token();
            int curPrecedence = OperationDefine.findPrecedence(op.type);
            if (curPrecedence == -1) {
                break;
            }
            tokenStream.nextToken();
            BaseNode rls = parsePrimaryExpression();
            if (opStack.size() != 0) {
                TokenType peek = opStack.peek();
                int peekPrecedence = OperationDefine.findPrecedence(peek);
                if (peekPrecedence == -1) {
                    throw SyntaxException.withSyntax("不支持的的二元操作符:" + peek);
                }
                if (curPrecedence <= peekPrecedence) {
                    BaseNode exp1 = valStack.pop();
                    BaseNode exp2 = valStack.pop();
                    TokenType binOp = opStack.pop();
                    if (TokenUtil.isLogicOperation(binOp)) {
                        LogicBinaryOperationNode logicBinaryOperationNode = new LogicBinaryOperationNode(
                                binOp, exp2, exp1, OperationDefine.findLogicOperation(binOp));
                        SyntaxCheckUtil.logicBinaryTypeCheck(logicBinaryOperationNode, parseContext, runtimeContext);
                        valStack.push(logicBinaryOperationNode);
                    } else {
                        BinaryOperationNode binaryOperationNode = new BinaryOperationNode(binOp, exp2, exp1,
                                OperationDefine.findOperation(binOp));
                        SyntaxCheckUtil.binaryTypeCheck(binaryOperationNode, parseContext, runtimeContext);
                        valStack.push(new BinaryOperationNode(binOp, exp2, exp1, OperationDefine.findOperation(binOp)));
                    }
                }
            }
            opStack.push(op.type);
            valStack.push(rls);
        }
        while (opStack.size() != 0) {
            BaseNode exp1 = valStack.pop();
            BaseNode exp2 = valStack.pop();
            TokenType binOp = opStack.pop();
            if (TokenUtil.isLogicOperation(binOp)) {
                LogicBinaryOperationNode logicBinaryOperationNode = new LogicBinaryOperationNode(binOp, exp2, exp1,
                        OperationDefine.findLogicOperation(binOp));
                SyntaxCheckUtil.logicBinaryTypeCheck(logicBinaryOperationNode, parseContext, runtimeContext);
                valStack.push(logicBinaryOperationNode);
            } else {
                BinaryOperationNode binaryOperationNode = new BinaryOperationNode(binOp, exp2, exp1,
                        OperationDefine.findOperation(binOp));
                SyntaxCheckUtil.binaryTypeCheck(binaryOperationNode, parseContext, runtimeContext);
                valStack.push(binaryOperationNode);
            }
        }
        return valStack.pop();
    }

    public BaseNode parsePrimaryExpression() {
        Token token = tokenStream.token();
        switch (token.type) {
            case IDENTIFIER:
            case OUT_IDENTIFIER:
            case LEFT_PAREN:
            case KEY_THIS:
                return parseCallLink(false);
            case ADD:
            case SUB:
            case NOT:
                return parseUnaryExpression();
            case INTEGER:
            case DOUBLE:
                return parseNumber();
            case STRING:
                return parseString();
            case KEY_FALSE:
            case KEY_TRUE:
                return parseBool();
            case KEY_FUN:
                return parseFunDefine();
            case KEY_NULL:
                return parseNull();
            case LEFT_SQUARE:
                return parseArray();
            case LEFT_BRACE:
                return parseRsonNode();
            case KEY_TYPEOF:
                return parseTypeOfNode();
        }
        throw SyntaxException.withSyntax("未知的表达式起始", token);
    }

    public BaseNode parseWhileStatement() {
        AssertUtil.assertToken(tokenStream, TokenType.KEY_WHILE);
        tokenStream.nextToken();
        BaseNode condition = parseExpression();
        BaseNode blockAST = null;
        if (tokenStream.token().type == TokenType.LEFT_BRACE) {
            blockAST = parseBlock();
        } else {
            blockAST = new LoopBlockNode(Collections.singletonList(parseStatement()));
        }
        return new WhileStatementNode(condition, blockAST);
    }

    public BaseNode parseIfStatement() {
        AssertUtil.assertToken(tokenStream, TokenType.KEY_IF);
        tokenStream.nextToken();

        BaseNode condition = parseExpression();
        BaseNode thenBlock = null;
        if (tokenStream.token().type == TokenType.LEFT_BRACE) {
            thenBlock = parseBlock();
        } else {
            thenBlock = new BlockNode(Collections.singletonList(parseStatement()));
        }
        BaseNode elseBlock = null;
        if (tokenStream.token().type == TokenType.KEY_ELSE) {
            Token token = tokenStream.nextToken();
            if (token.type == TokenType.LEFT_BRACE) {
                elseBlock = parseBlock();
            } else {
                elseBlock = new BlockNode(Collections.singletonList(parseStatement()));
            }
        }
        return new IfStatementNode(condition, thenBlock, elseBlock);
    }

    public BaseNode parseVariableDefine() {
        // eat var
        AssertUtil.assertToken(tokenStream, TokenType.KEY_VAR);
        tokenStream.nextToken();

        AssertUtil.assertToken(tokenStream, TokenType.IDENTIFIER);

        Token name = tokenStream.token();
        tokenStream.nextToken();

        AssertUtil.assertToken(tokenStream, TokenType.ASSIGN);
        tokenStream.nextToken();

        BaseNode exp = parseExpression();
        return new VariableDefineNode(TokenUtil.buildNameNode(name), exp);
    }

    public BaseNode parseFunDefine() {
        // eat fun
        AssertUtil.assertToken(tokenStream, TokenType.KEY_FUN);
        tokenStream.nextToken();

        String name = null;
        if (tokenStream.token().type == TokenType.IDENTIFIER) {
            name = tokenStream.token().name;
            tokenStream.nextToken();
        }
        // eat (
        AssertUtil.assertToken(tokenStream, TokenType.LEFT_PAREN);
        tokenStream.nextToken();

        List<BaseNode> param = new ArrayList<BaseNode>();
        if (tokenStream.token().type != TokenType.RIGHT_PAREN) {
            AssertUtil.assertToken(tokenStream, TokenType.IDENTIFIER);
            Token token = tokenStream.token();
            param.add(TokenUtil.buildNameNode(token));

            tokenStream.nextToken();
        }
        while (tokenStream.token().type != TokenType.RIGHT_PAREN) {
            AssertUtil.assertToken(tokenStream, TokenType.COMMA);
            tokenStream.nextToken();

            AssertUtil.assertToken(tokenStream, TokenType.IDENTIFIER);
            Token token = tokenStream.token();
            param.add(TokenUtil.buildNameNode(token));

            tokenStream.nextToken();
        }
        // eat )
        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_PAREN);
        tokenStream.nextToken();

        BaseNode block = parseBlock();

        return new ClosureDefineNode(name, param, block);
    }

    public BaseNode parseBlock() {
        AssertUtil.assertToken(tokenStream, TokenType.LEFT_BRACE);
        tokenStream.nextToken();

        List<BaseNode> blocks = new ArrayList<BaseNode>();

        while (tokenStream.token().type != TokenType.EOF
                && tokenStream.token().type != TokenType.RIGHT_BRACE) {
            blocks.add(parseStatement());
        }

        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_BRACE);
        tokenStream.nextToken();

        return new BlockNode(blocks);
    }

    public BaseNode parseUnaryExpression() {
        Token token = tokenStream.token();
        tokenStream.nextToken();
        if (token.type == TokenType.ADD || token.type == TokenType.SUB) {
            return new UnaryOperationNode(token.type, parsePrimaryExpression(),
                    token.type == TokenType.ADD ? new UnarySubOperation() : new UnaryAddOperation());
        } else if (token.type == TokenType.NOT) {
            return new LogicBinaryOperationNode(TokenType.NOT, parsePrimaryExpression(), null,
                    OperationDefine.findLogicOperation(TokenType.NOT));
        }
        throw SyntaxException.withSyntax("不支持的单目运算符", token);
    }

    public BaseNode parseCallLink(boolean isStatement) {
        BaseNode firstNode = null;
        if (tokenStream.token().type == TokenType.IDENTIFIER
                || tokenStream.token().type == TokenType.OUT_IDENTIFIER) {
            firstNode = TokenUtil.buildNameNode(tokenStream.token());
            tokenStream.nextToken();
        } else if (tokenStream.token().type == TokenType.LEFT_PAREN) {
            firstNode = parseParen();
        } else if (tokenStream.token().type == TokenType.KEY_THIS) {
            firstNode = parseThis();
        } else {
            firstNode = parsePrimaryExpression();
        }

        List<OperationNode> calls = new ArrayList<OperationNode>();

        while (tokenStream.token().type == TokenType.LEFT_PAREN
                || tokenStream.token().type == TokenType.LEFT_SQUARE
                || tokenStream.token().type == TokenType.DOT) {
            switch (tokenStream.token().type) {
                case LEFT_PAREN:
                    calls.add((OperationNode) parseCall());
                    break;
                case LEFT_SQUARE:
                    calls.add((OperationNode) parseIndex());
                    break;
                case DOT:
                    calls.add((OperationNode) parseDot());
                    break;
            }
        }

        CallLinkNode callLinkNode = new CallLinkNode(firstNode, calls);

        if (tokenStream.token().type == TokenType.ASSIGN) {
            if (!isStatement) {
                throw SyntaxException.withSyntax("表达式内不允许出现赋值语句");
            }
            if (firstNode instanceof OutNameNode) {
                throw SyntaxException.withSyntax("不能对外部变量进行赋值: $" + ((OutNameNode) firstNode).name.name);
            }
            return parseCallLinkAssignNode(callLinkNode);
        }

        return callLinkNode;
    }

    public BaseNode parseDot() {
        AssertUtil.assertToken(tokenStream, TokenType.DOT);
        tokenStream.nextToken();

        AssertUtil.assertToken(tokenStream, TokenType.IDENTIFIER);
        Token name = tokenStream.token();
        tokenStream.nextToken();

        if (tokenStream.token().type == TokenType.LEFT_PAREN) {
            tokenStream.nextToken();
            List<BaseNode> param = new ArrayList<BaseNode>();
            if (tokenStream.token().type != TokenType.RIGHT_PAREN) {
                param.add(parseExpression());
            }
            while (tokenStream.token().type != TokenType.RIGHT_PAREN) {
                AssertUtil.assertToken(tokenStream, TokenType.COMMA);
                tokenStream.nextToken();
                param.add(parseExpression());
            }
            AssertUtil.assertToken(tokenStream, TokenType.RIGHT_PAREN);
            tokenStream.nextToken();

            DotNode dotNode = new DotNode(TokenType.CALL, name.name, param);
            dotNode.putAssignOperation(OperationDefine.findAssignOperation(TokenType.DOT));
            return dotNode;
        } else {
            DotNode dotNode = new DotNode(TokenType.IDENTIFIER, name.name, null);
            dotNode.putAssignOperation(OperationDefine.findAssignOperation(TokenType.DOT));
            return dotNode;
        }
    }

    public BaseNode parseIndex() {
        AssertUtil.assertToken(tokenStream, TokenType.LEFT_SQUARE);
        tokenStream.nextToken();

        BaseNode index = parsePrimaryExpression();

        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_SQUARE);
        tokenStream.nextToken();

        IndexNode indexNode = new IndexNode(index);
        indexNode.putOperation(OperationDefine.findOperation(TokenType.INDEX));
        indexNode.putAssignOperation(OperationDefine.findAssignOperation(TokenType.INDEX));

        return indexNode;
    }

    public BaseNode parseCall() {
        AssertUtil.assertToken(tokenStream, TokenType.LEFT_PAREN);
        tokenStream.nextToken();
        if (tokenStream.token().type == TokenType.RIGHT_PAREN) {
            tokenStream.nextToken();
            CallNode callNode = new CallNode(Collections.<BaseNode>emptyList());
            callNode.putOperation(OperationDefine.findOperation(TokenType.CALL));
            return callNode;
        }
        BaseNode param1 = parseExpression();

        List<BaseNode> params = new ArrayList<BaseNode>();
        params.add(param1);
        while (tokenStream.token().type != TokenType.EOF && tokenStream.token().type != TokenType.RIGHT_PAREN) {
            AssertUtil.assertToken(tokenStream, TokenType.COMMA);
            tokenStream.nextToken();
            params.add(parseExpression());
        }
        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_PAREN);
        tokenStream.nextToken();

        CallNode callNode = new CallNode(params);
        callNode.putOperation(OperationDefine.findOperation(TokenType.CALL));
        return callNode;
    }

    public BaseNode parseParen() {
        AssertUtil.assertToken(tokenStream, TokenType.LEFT_PAREN);
        tokenStream.nextToken();
        BaseNode ast = parseExpression();
        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_PAREN);
        tokenStream.nextToken();
        return ast;
    }

    public BaseNode parseNumber() {
        Token token = tokenStream.token();
        tokenStream.nextToken();
        if (token.type == TokenType.INTEGER) {
            return new IntegerNode(Integer.parseInt(token.name));
        }
        if (token.type == TokenType.DOUBLE) {
            return new DoubleNode(Double.parseDouble(token.name));
        }
        throw SyntaxException.withSyntax("需要一个数字", token);
    }

    public BaseNode parseString() {
        AssertUtil.assertToken(tokenStream, TokenType.STRING);
        Token token = tokenStream.token();
        tokenStream.nextToken();
        return new StringNode(token.name);
    }

    public BaseNode parseBool() {
        Token token = tokenStream.token();
        if (token.type != TokenType.KEY_FALSE && token.type != TokenType.KEY_TRUE) {
            throw SyntaxException.withSyntax("需要一个bool", token);
        }
        tokenStream.nextToken();
        return new BoolNode(Boolean.parseBoolean(token.name));
    }

    public BaseNode parseNull() {

        AssertUtil.assertToken(tokenStream, TokenType.KEY_NULL);
        tokenStream.nextToken();

        return NullNode.NULL_NODE;
    }

    public BaseNode parseContinue() {
        AssertUtil.assertToken(tokenStream, TokenType.KEY_CONTINUE);
        tokenStream.nextToken();
        return new ContinueNode();
    }

    public BaseNode parseBreak() {
        AssertUtil.assertToken(tokenStream, TokenType.KEY_BREAK);
        tokenStream.nextToken();
        return new BreakNode();
    }

    public BaseNode parseReturn() {
        AssertUtil.assertToken(tokenStream, TokenType.KEY_RETURN);
        tokenStream.nextToken();

        List<BaseNode> param = new ArrayList<BaseNode>();
        if (tokenStream.token().type != TokenType.SEMICOLON) {
            param.add(parseExpression());
        }
        while (tokenStream.token().type != TokenType.SEMICOLON) {
            AssertUtil.assertToken(tokenStream, TokenType.COMMA);
            tokenStream.nextToken();
            param.add(parseExpression());
        }
        return new ReturnNode(param);
    }

    public BaseNode parseArray() {

        AssertUtil.assertToken(tokenStream, TokenType.LEFT_SQUARE);
        tokenStream.nextToken();

        List<BaseNode> arrValues = new ArrayList<BaseNode>();
        if (tokenStream.token().type != TokenType.RIGHT_SQUARE) {
            arrValues.add(parseExpression());
        }
        while (tokenStream.token().type != TokenType.RIGHT_SQUARE) {

            AssertUtil.assertToken(tokenStream, TokenType.COMMA);
            tokenStream.nextToken();

            arrValues.add(parseExpression());
        }
        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_SQUARE);
        tokenStream.nextToken();
        return new ArrayNode(arrValues);
    }

    public BaseNode parseTypeOfNode() {
        AssertUtil.assertToken(tokenStream, TokenType.KEY_TYPEOF);
        tokenStream.nextToken();
        BaseNode exp = parseExpression();
        return new TypeOfNode(exp);
    }

    public BaseNode parseRsonNode() {
        AssertUtil.assertToken(tokenStream, TokenType.LEFT_BRACE);
        tokenStream.nextToken();
        Map<String, BaseNode> properties = new HashMap<String, BaseNode>();
        if (tokenStream.token().type != TokenType.RIGHT_BRACE) {
            if (tokenStream.token().type != TokenType.IDENTIFIER
                    && tokenStream.token().type != TokenType.STRING) {
                throw SyntaxException.withSyntax("无效的key", tokenStream.token());
            }
            Token name = tokenStream.token();
            tokenStream.nextToken();

            AssertUtil.assertToken(tokenStream, TokenType.COLON);
            tokenStream.nextToken();
            properties.put(name.name, parseExpression());
        }
        while (tokenStream.token().type != TokenType.RIGHT_BRACE) {
            AssertUtil.assertToken(tokenStream, TokenType.COMMA);
            tokenStream.nextToken();

            if (tokenStream.token().type == TokenType.RIGHT_BRACE) {
                break;
            }
            if (tokenStream.token().type != TokenType.IDENTIFIER
                    && tokenStream.token().type != TokenType.STRING) {
                throw SyntaxException.withSyntax("无效的key", tokenStream.token());
            }
            Token name = tokenStream.token();
            tokenStream.nextToken();
            AssertUtil.assertToken(tokenStream, TokenType.COLON);
            tokenStream.nextToken();
            properties.put(name.name, parseExpression());
        }

        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_BRACE);
        tokenStream.nextToken();
        return new RsonNode(properties);
    }

    public BaseNode parseThis() {
        AssertUtil.assertToken(tokenStream, TokenType.KEY_THIS);
        tokenStream.nextToken();
        return new ThisNode();
    }

    public BaseNode parseImport() {
        AssertUtil.assertToken(tokenStream, TokenType.KEY_IMPORT);
        tokenStream.nextToken();
        AssertUtil.assertToken(tokenStream, TokenType.STRING);
        String path = tokenStream.token().name;
        Token aliasToken = tokenStream.nextToken();
        AssertUtil.assertToken(tokenStream, TokenType.IDENTIFIER);
        tokenStream.nextToken();
        AssertUtil.assertToken(tokenStream, TokenType.SEMICOLON);
        tokenStream.nextToken();
        return new ImportNode(path, aliasToken.name);
    }

}
