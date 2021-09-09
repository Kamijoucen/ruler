package com.kamijoucen.ruler.parse;

import com.kamijoucen.ruler.ast.*;
import com.kamijoucen.ruler.ast.op.CallNode;
import com.kamijoucen.ruler.ast.op.DotNode;
import com.kamijoucen.ruler.ast.op.IndexNode;
import com.kamijoucen.ruler.ast.op.OperationNode;
import com.kamijoucen.ruler.ast.statement.*;
import com.kamijoucen.ruler.common.RStack;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.module.RulerModule;
import com.kamijoucen.ruler.runtime.OperationDefine;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.util.SyntaxCheckUtil;
import com.kamijoucen.ruler.util.TokenUtil;

import java.util.*;

public class DefaultParser implements Parser {

    private Lexical lexical;

    private List<BaseNode> statements;

    private RulerModule file;

    public DefaultParser(Lexical lexical, RulerModule file) {
        this.lexical = lexical;
        this.file = file;
        this.statements = new ArrayList<BaseNode>();
    }

    @Override
    public List<BaseNode> parse() {

        lexical.nextToken();

        if (lexical.getToken().type == TokenType.KEY_IMPORT) {
            List<ImportNode> imports = parseImports();
            file.setImportList(imports);
        } else {
            file.setImportList(Collections.<ImportNode>emptyList());
        }
        SyntaxCheckUtil.availableImport(file);

        while (lexical.getToken().type != TokenType.EOF) {
            statements.add(parseStatement());
        }

        file.setStatements(statements);

        return statements;
    }

    public List<ImportNode> parseImports() {

        if (lexical.getToken().type != TokenType.KEY_IMPORT) {
            return Collections.emptyList();
        }
        List<ImportNode> imports = new ArrayList<ImportNode>();

        while (lexical.getToken().type == TokenType.KEY_IMPORT) {
            BaseNode importNode = parseImport();
            statements.add(importNode);
            imports.add((ImportNode) importNode);
        }
        return imports;
    }


    public BaseNode parseStatement() {
        Token token = lexical.getToken();

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
                statement = parseContinueAST();
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
            AssertUtil.assertToken(lexical, TokenType.SEMICOLON);
            lexical.nextToken();
        }
        return statement;
    }

    public BaseNode parseCallLinkAssignNode(BaseNode callLinkNode) {

        AssertUtil.assertToken(lexical.getToken(), TokenType.ASSIGN);
        lexical.nextToken();

        BaseNode expression = parseExpression();

        return new AssignNode(callLinkNode, expression);
    }

    public BaseNode parseExpression() {

        RStack<BaseNode> valStack = new RStack<BaseNode>();

        RStack<TokenType> opStack = new RStack<TokenType>();

        valStack.push(parsePrimaryExpression()); // first exp

        while (true) {

            Token op = lexical.getToken();

            int curPrecedence = OperationDefine.findPrecedence(op.type);
            if (curPrecedence == -1) {
                break;
            }
            lexical.nextToken();

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
                        valStack.push(new LogicBinaryOperationNode(binOp, exp2, exp1,
                                OperationDefine.findLogicOperation(binOp)));
                    } else {
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
                valStack.push(
                        new LogicBinaryOperationNode(binOp, exp2, exp1, OperationDefine.findLogicOperation(binOp)));
            } else {
                valStack.push(new BinaryOperationNode(binOp, exp2, exp1, OperationDefine.findOperation(binOp)));
            }
        }
        return valStack.pop();
    }

    public BaseNode parsePrimaryExpression() {

        Token token = lexical.getToken();

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
        }
        throw SyntaxException.withSyntax("未知的表达式起始", token);
    }

    public BaseNode parseWhileStatement() {

        AssertUtil.assertToken(lexical, TokenType.KEY_WHILE);
        lexical.nextToken();

        BaseNode condition = parseExpression();

        BaseNode blockAST = null;

        if (lexical.getToken().type == TokenType.LEFT_BRACE) {
            blockAST = parseBlock();
        } else {
            blockAST = new BlockNode(Collections.singletonList(parseStatement()));
        }

        return new WhileStatementNode(condition, blockAST);
    }

    public BaseNode parseIfStatement() {

        AssertUtil.assertToken(lexical, TokenType.KEY_IF);
        lexical.nextToken();

        BaseNode condition = parseExpression();

        BaseNode thenBlock = null;
        if (lexical.getToken().type == TokenType.LEFT_BRACE) {
            thenBlock = parseBlock();
        } else {
            thenBlock = new BlockNode(Collections.singletonList(parseStatement()));
        }

        BaseNode elseBlock = null;
        if (lexical.getToken().type == TokenType.KEY_ELSE) {
            Token token = lexical.nextToken();
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
        AssertUtil.assertToken(lexical, TokenType.KEY_VAR);
        lexical.nextToken();

        AssertUtil.assertToken(lexical, TokenType.IDENTIFIER);

        Token name = lexical.getToken();
        lexical.nextToken();

        AssertUtil.assertToken(lexical, TokenType.ASSIGN);
        lexical.nextToken();

        BaseNode exp = parseExpression();

        return new VariableDefineNode(TokenUtil.buildNameNode(name), exp);
    }

    public BaseNode parseFunDefine() {

        // eat fun
        AssertUtil.assertToken(lexical, TokenType.KEY_FUN);
        lexical.nextToken();

        String name = null;
        if (lexical.getToken().type == TokenType.IDENTIFIER) {
            name = lexical.getToken().name;
            lexical.nextToken();
        }

        // eat (
        AssertUtil.assertToken(lexical, TokenType.LEFT_PAREN);
        lexical.nextToken();

        List<BaseNode> param = new ArrayList<BaseNode>();

        if (lexical.getToken().type != TokenType.RIGHT_PAREN) {
            AssertUtil.assertToken(lexical, TokenType.IDENTIFIER);
            Token token = lexical.getToken();
            param.add(TokenUtil.buildNameNode(token));

            lexical.nextToken();
        }

        while (lexical.getToken().type != TokenType.RIGHT_PAREN) {
            AssertUtil.assertToken(lexical, TokenType.COMMA);
            lexical.nextToken();

            AssertUtil.assertToken(lexical, TokenType.IDENTIFIER);
            Token token = lexical.getToken();
            param.add(TokenUtil.buildNameNode(token));

            lexical.nextToken();
        }

        // eat )
        AssertUtil.assertToken(lexical, TokenType.RIGHT_PAREN);
        lexical.nextToken();

        BaseNode block = parseBlock();

        return new ClosureDefineNode(name, param, block);
    }

    public BaseNode parseBlock() {

        AssertUtil.assertToken(lexical, TokenType.LEFT_BRACE);
        lexical.nextToken();

        List<BaseNode> blocks = new ArrayList<BaseNode>();

        while (lexical.getToken().type != TokenType.EOF
                && lexical.getToken().type != TokenType.RIGHT_BRACE) {
            blocks.add(parseStatement());
        }

        AssertUtil.assertToken(lexical, TokenType.RIGHT_BRACE);
        lexical.nextToken();

        return new BlockNode(blocks);
    }

    public BaseNode parseUnaryExpression() {
        Token token = lexical.getToken();
        lexical.nextToken();

        if (token.type == TokenType.ADD || token.type == TokenType.SUB) {
            return new UnaryOperationNode(token.type, parsePrimaryExpression());
        } else if (token.type == TokenType.NOT) {
            return new LogicBinaryOperationNode(TokenType.NOT, parsePrimaryExpression(), null,
                    OperationDefine.findLogicOperation(TokenType.NOT));
        }
        throw SyntaxException.withSyntax("不支持的单目运算符", token);
    }

    public BaseNode parseCallLink(boolean isStatement) {

        BaseNode firstNode = null;
        if (lexical.getToken().type == TokenType.IDENTIFIER
                || lexical.getToken().type == TokenType.OUT_IDENTIFIER) {
            firstNode = TokenUtil.buildNameNode(lexical.getToken());
            lexical.nextToken();
        } else if (lexical.getToken().type == TokenType.LEFT_PAREN) {
            firstNode = parseParen();
        } else if (lexical.getToken().type == TokenType.KEY_THIS) {
            firstNode = parseThis();
        } else {
            firstNode = parsePrimaryExpression();
        }

        List<OperationNode> calls = new ArrayList<OperationNode>();

        while (lexical.getToken().type == TokenType.LEFT_PAREN
                || lexical.getToken().type == TokenType.LEFT_SQUARE
                || lexical.getToken().type == TokenType.DOT) {
            switch (lexical.getToken().type) {
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

        if (lexical.getToken().type == TokenType.ASSIGN) {
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

        AssertUtil.assertToken(lexical, TokenType.DOT);
        lexical.nextToken();

        AssertUtil.assertToken(lexical, TokenType.IDENTIFIER);
        Token name = lexical.getToken();
        lexical.nextToken();

        if (lexical.getToken().type == TokenType.LEFT_PAREN) {

            lexical.nextToken();

            List<BaseNode> param = new ArrayList<BaseNode>();

            if (lexical.getToken().type != TokenType.RIGHT_PAREN) {
                param.add(parseExpression());
            }

            while (lexical.getToken().type != TokenType.RIGHT_PAREN) {
                AssertUtil.assertToken(lexical, TokenType.COMMA);
                lexical.nextToken();
                param.add(parseExpression());
            }

            AssertUtil.assertToken(lexical, TokenType.RIGHT_PAREN);
            lexical.nextToken();

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

        AssertUtil.assertToken(lexical, TokenType.LEFT_SQUARE);
        lexical.nextToken();

        BaseNode index = parsePrimaryExpression();

        AssertUtil.assertToken(lexical, TokenType.RIGHT_SQUARE);
        lexical.nextToken();

        IndexNode indexNode = new IndexNode(index);
        indexNode.putOperation(OperationDefine.findOperation(TokenType.INDEX));
        indexNode.putAssignOperation(OperationDefine.findAssignOperation(TokenType.INDEX));

        return indexNode;
    }

    public BaseNode parseCall() {

        AssertUtil.assertToken(lexical, TokenType.LEFT_PAREN);
        lexical.nextToken();

        if (lexical.getToken().type == TokenType.RIGHT_PAREN) {
            lexical.nextToken();
            CallNode callNode = new CallNode(Collections.<BaseNode>emptyList());
            callNode.putOperation(OperationDefine.findOperation(TokenType.CALL));
            return callNode;
        }

        BaseNode param1 = parseExpression();

        List<BaseNode> params = new ArrayList<BaseNode>();
        params.add(param1);

        while (lexical.getToken().type != TokenType.EOF && lexical.getToken().type != TokenType.RIGHT_PAREN) {
            AssertUtil.assertToken(lexical, TokenType.COMMA);
            lexical.nextToken();
            params.add(parseExpression());
        }

        AssertUtil.assertToken(lexical, TokenType.RIGHT_PAREN);
        lexical.nextToken();

        CallNode callNode = new CallNode(params);
        callNode.putOperation(OperationDefine.findOperation(TokenType.CALL));

        return callNode;
    }

    public BaseNode parseParen() {

        AssertUtil.assertToken(lexical, TokenType.LEFT_PAREN);
        lexical.nextToken();

        BaseNode ast = parseExpression();

        AssertUtil.assertToken(lexical, TokenType.RIGHT_PAREN);
        lexical.nextToken();

        return ast;
    }

    public BaseNode parseNumber() {

        Token token = lexical.getToken();
        lexical.nextToken();

        if (token.type == TokenType.INTEGER) {
            return new IntegerNode(Integer.parseInt(token.name));
        }

        if (token.type == TokenType.DOUBLE) {
            return new DoubleNode(Double.parseDouble(token.name));
        }

        throw SyntaxException.withSyntax("需要一个数字", token);
    }

    public BaseNode parseString() {

        AssertUtil.assertToken(lexical, TokenType.STRING);
        Token token = lexical.getToken();

        lexical.nextToken();

        return new StringNode(token.name);
    }

    public BaseNode parseBool() {

        Token token = lexical.getToken();

        if (token.type != TokenType.KEY_FALSE && token.type != TokenType.KEY_TRUE) {
            throw SyntaxException.withSyntax("需要一个bool", token);
        }
        lexical.nextToken();

        return new BoolNode(Boolean.parseBoolean(token.name));
    }

    public BaseNode parseNull() {

        AssertUtil.assertToken(lexical, TokenType.KEY_NULL);
        lexical.nextToken();

        return NullNode.NULL_NODE;
    }

    public BaseNode parseContinueAST() {

        AssertUtil.assertToken(lexical, TokenType.KEY_CONTINUE);
        lexical.nextToken();

        return new ContinueNode();
    }

    public BaseNode parseBreak() {

        AssertUtil.assertToken(lexical, TokenType.KEY_BREAK);
        lexical.nextToken();

        return new BreakNode();
    }

    public BaseNode parseReturn() {

        AssertUtil.assertToken(lexical, TokenType.KEY_RETURN);
        lexical.nextToken();

        List<BaseNode> param = new ArrayList<BaseNode>();

        if (lexical.getToken().type != TokenType.SEMICOLON) {
            param.add(parseExpression());
        }

        while (lexical.getToken().type != TokenType.SEMICOLON) {
            AssertUtil.assertToken(lexical, TokenType.COMMA);
            lexical.nextToken();
            param.add(parseExpression());
        }

        return new ReturnNode(param);
    }

    public BaseNode parseArray() {

        AssertUtil.assertToken(lexical, TokenType.LEFT_SQUARE);
        lexical.nextToken();

        List<BaseNode> arrValues = new ArrayList<BaseNode>();

        if (lexical.getToken().type != TokenType.RIGHT_SQUARE) {
            arrValues.add(parseExpression());
        }

        while (lexical.getToken().type != TokenType.RIGHT_SQUARE) {

            AssertUtil.assertToken(lexical, TokenType.COMMA);
            lexical.nextToken();

            arrValues.add(parseExpression());
        }

        AssertUtil.assertToken(lexical, TokenType.RIGHT_SQUARE);
        lexical.nextToken();

        return new ArrayNode(arrValues);
    }

    public BaseNode parseRsonNode() {

        AssertUtil.assertToken(lexical, TokenType.LEFT_BRACE);
        lexical.nextToken();

        Map<String, BaseNode> properties = new HashMap<String, BaseNode>();

        if (lexical.getToken().type != TokenType.RIGHT_BRACE) {

            if (lexical.getToken().type != TokenType.IDENTIFIER
                    && lexical.getToken().type != TokenType.STRING) {
                throw SyntaxException.withSyntax("无效的key", lexical.getToken());
            }

            Token name = lexical.getToken();
            lexical.nextToken();

            AssertUtil.assertToken(lexical, TokenType.COLON);
            lexical.nextToken();

            properties.put(name.name, parseExpression());

        }

        while (lexical.getToken().type != TokenType.RIGHT_BRACE) {

            AssertUtil.assertToken(lexical, TokenType.COMMA);
            lexical.nextToken();

            if (lexical.getToken().type == TokenType.RIGHT_BRACE) {
                break;
            }

            if (lexical.getToken().type != TokenType.IDENTIFIER
                    && lexical.getToken().type != TokenType.STRING) {
                throw SyntaxException.withSyntax("无效的key", lexical.getToken());
            }

            Token name = lexical.getToken();
            lexical.nextToken();

            AssertUtil.assertToken(lexical, TokenType.COLON);
            lexical.nextToken();

            properties.put(name.name, parseExpression());
        }

        AssertUtil.assertToken(lexical, TokenType.RIGHT_BRACE);
        lexical.nextToken();

        return new RsonNode(properties);
    }

    public BaseNode parseThis() {
        AssertUtil.assertToken(lexical, TokenType.KEY_THIS);

        lexical.nextToken();

        return new ThisNode();
    }

    public BaseNode parseImport() {

        AssertUtil.assertToken(lexical, TokenType.KEY_IMPORT);
        lexical.nextToken();

        AssertUtil.assertToken(lexical, TokenType.STRING);

        String path = lexical.getToken().name;

        Token aliasToken = lexical.nextToken();

        AssertUtil.assertToken(lexical, TokenType.IDENTIFIER);
        lexical.nextToken();

        AssertUtil.assertToken(lexical, TokenType.SEMICOLON);
        lexical.nextToken();

        return new ImportNode(path, aliasToken.name);
    }


    public BaseNode parseInitNode() {

        AssertUtil.assertToken(lexical, TokenType.KEY_INIT);
        lexical.nextToken();

        BaseNode blockNode = parseBlock();

        return null;
    }

}
