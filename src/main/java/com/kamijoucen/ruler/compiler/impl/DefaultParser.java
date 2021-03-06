package com.kamijoucen.ruler.compiler.impl;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.OperationNode;
import com.kamijoucen.ruler.ast.expression.*;
import com.kamijoucen.ruler.ast.facotr.*;
import com.kamijoucen.ruler.common.OperationDefine;
import com.kamijoucen.ruler.common.RStack;
import com.kamijoucen.ruler.compiler.Parser;
import com.kamijoucen.ruler.compiler.TokenStream;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.exception.IllegalOperationException;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.operation.UnaryAddOperation;
import com.kamijoucen.ruler.operation.UnarySubOperation;
import com.kamijoucen.ruler.runtime.RuntimeContext;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.type.ArrayType;
import com.kamijoucen.ruler.type.UnknownType;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.util.SyntaxCheckUtil;
import com.kamijoucen.ruler.util.TokenUtil;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.*;

public class DefaultParser implements Parser {

    private final RulerConfiguration configuration;
    private ParseContext parseContext;
    private RuntimeContext runtimeContext;
    private final List<BaseNode> statements;
    private final TokenStream tokenStream;

    public DefaultParser(TokenStream tokenStream, RulerConfiguration configuration) {
        this.tokenStream = tokenStream;
        this.configuration = configuration;
        this.statements = new ArrayList<BaseNode>();
        initContext();
    }

    private void initContext() {
        this.parseContext = new ParseContext(configuration.getTypeCheckVisitor());
        this.runtimeContext = configuration.createDefaultRuntimeContext(null);
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
            statements.add(parseStatement(true));
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
            imports.add((ImportNode) importNode);
        }
        return imports;
    }

    public BaseNode parseStatement(boolean isRoot) {

        Token token = tokenStream.token();
        BaseNode statement = null;

        if (token.type == TokenType.KEY_RULE) {
            if (!isRoot) {
                throw new IllegalOperationException("");
            }
            statement = parseRuleBlock();
        } else {
            statement = parseExpression();
        }
        if (statement == null) {
            throw SyntaxException.withSyntax("??????????????????" + token.name);
        }

        if (token.type != TokenType.KEY_FUN
                && token.type != TokenType.KEY_IF
                && token.type != TokenType.KEY_FOR
                && token.type != TokenType.KEY_WHILE
                && token.type != TokenType.KEY_RULE) {
            AssertUtil.assertToken(tokenStream, TokenType.SEMICOLON);
            tokenStream.nextToken();
        }
        return statement;
    }

    public BaseNode parseCallLinkAssignNode(BaseNode callLinkNode) {
        Token assignToken = tokenStream.token();
        AssertUtil.assertToken(assignToken, TokenType.ASSIGN);
        tokenStream.nextToken();
        BaseNode expression = parseExpression();
        return new AssignNode(callLinkNode, expression, assignToken.location);
    }

    public BaseNode parseExpression() {

        RStack<BaseNode> valStack = new RStack<BaseNode>();
        RStack<Token> opStack = new RStack<Token>();

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
                Token peek = opStack.peek();
                int peekPrecedence = OperationDefine.findPrecedence(peek.type);
                if (peekPrecedence == -1) {
                    throw SyntaxException.withSyntax("??????????????????????????????:" + peek);
                }
                if (curPrecedence <= peekPrecedence) {
                    BaseNode exp1 = valStack.pop();
                    BaseNode exp2 = valStack.pop();
                    Token binOp = opStack.pop();
                    if (TokenUtil.isLogicOperation(binOp.type)) {
                        LogicBinaryOperationNode logicBinaryOperationNode = new LogicBinaryOperationNode(
                                binOp.type, exp2, exp1, OperationDefine.findLogicOperation(binOp.type), binOp.location);
                        SyntaxCheckUtil.logicBinaryTypeCheck(logicBinaryOperationNode, parseContext, runtimeContext);
                        valStack.push(logicBinaryOperationNode);
                    } else {
                        BinaryOperationNode binaryOperationNode = new BinaryOperationNode(binOp.type, exp2, exp1,
                                OperationDefine.findOperation(binOp.type), binOp.location);
                        SyntaxCheckUtil.binaryTypeCheck(binaryOperationNode, parseContext, runtimeContext);
                        valStack.push(new BinaryOperationNode(binOp.type, exp2, exp1,
                                OperationDefine.findOperation(binOp.type), binOp.location));
                    }
                }
            }
            opStack.push(op);
            valStack.push(rls);
        }
        while (opStack.size() != 0) {
            BaseNode exp1 = valStack.pop();
            BaseNode exp2 = valStack.pop();
            Token binOp = opStack.pop();
            if (TokenUtil.isLogicOperation(binOp.type)) {
                LogicBinaryOperationNode logicBinaryOperationNode = new LogicBinaryOperationNode(binOp.type, exp2, exp1,
                        OperationDefine.findLogicOperation(binOp.type), binOp.location);
                SyntaxCheckUtil.logicBinaryTypeCheck(logicBinaryOperationNode, parseContext, runtimeContext);
                valStack.push(logicBinaryOperationNode);
            } else {
                BinaryOperationNode binaryOperationNode = new BinaryOperationNode(binOp.type, exp2, exp1,
                        OperationDefine.findOperation(binOp.type), binOp.location);
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
                return parseCallLink();
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
            case RIGHT_PAREN:
                return parseParen();
            case LEFT_BRACE:
                return parseRsonNode();
            case KEY_TYPEOF:
                return parseTypeOfNode();
            case KEY_IF:
                return parseIfStatement();
            case KEY_WHILE:
                return parseWhileStatement();
            case KEY_FOR:
                return parseForEachStatement();
            case KEY_RETURN:
                return parseReturn();
            case KEY_BREAK:
                return parseBreak();
            case KEY_CONTINUE:
                return parseContinue();
            case KEY_VAR:
                return parseVariableDefine();
        }
        throw SyntaxException.withSyntax("????????????????????????", token);
    }

    public BaseNode parseForEachStatement() {
        // key
        Token forToken = tokenStream.token();
        AssertUtil.assertToken(forToken, TokenType.KEY_FOR);
        tokenStream.nextToken();
        // temp var
        AssertUtil.assertToken(tokenStream, TokenType.IDENTIFIER);
        Token name = tokenStream.token();
        tokenStream.nextToken();
        AssertUtil.assertToken(tokenStream, TokenType.KEY_IN);
        tokenStream.nextToken();
        BaseNode arrayExp = parseExpression();
        // type check
        BaseValue typeCheckValue = arrayExp.typeCheck(runtimeContext, null);
        if (typeCheckValue.getType() != UnknownType.INSTANCE.getType()
                && typeCheckValue.getType() != ArrayType.INSTANCE.getType()) {
            throw SyntaxException.withSyntax("The value of the expression must be an array!");
        }
        BaseNode blockNode = null;
        if (tokenStream.token().type == TokenType.LEFT_BRACE) {
            blockNode = parseBlock(true);
        } else {
            BaseNode statement = parseStatement(false);
            blockNode = new LoopBlockNode(Collections.singletonList(statement), statement.getLocation());
        }
        return new ForEachStatementNode(name, arrayExp, blockNode, forToken.location);
    }

    public BaseNode parseWhileStatement() {
        Token whileToken = tokenStream.token();
        AssertUtil.assertToken(whileToken, TokenType.KEY_WHILE);
        tokenStream.nextToken();
        BaseNode condition = parseExpression();
        BaseNode blockAST;
        if (tokenStream.token().type == TokenType.LEFT_BRACE) {
            blockAST = parseBlock(true);
        } else {
            BaseNode statement = parseStatement(false);
            blockAST = new LoopBlockNode(Collections.singletonList(statement), statement.getLocation());
        }
        return new WhileStatementNode(condition, blockAST, whileToken.location);
    }

    public BaseNode parseIfStatement() {

        Token ifToken = tokenStream.token();

        AssertUtil.assertToken(ifToken, TokenType.KEY_IF);
        tokenStream.nextToken();

        BaseNode condition = parseExpression();
        BaseNode thenBlock = null;
        if (tokenStream.token().type == TokenType.LEFT_BRACE) {
            thenBlock = parseBlock(false);
        } else {
            BaseNode statement = parseStatement(false);
            thenBlock = new BlockNode(Collections.singletonList(statement), statement.getLocation());
        }
        BaseNode elseBlock = null;
        if (tokenStream.token().type == TokenType.KEY_ELSE) {
            Token token = tokenStream.nextToken();
            if (token.type == TokenType.LEFT_BRACE) {
                elseBlock = parseBlock(false);
            } else {
                BaseNode statement = parseStatement(false);
                elseBlock = new BlockNode(Collections.singletonList(statement), statement.getLocation());
            }
        }
        return new IfStatementNode(condition, thenBlock, elseBlock, ifToken.location);
    }

    public BaseNode parseVariableDefine() {
        Token varToken = tokenStream.token();
        // eat var
        AssertUtil.assertToken(varToken, TokenType.KEY_VAR);
        tokenStream.nextToken();
        AssertUtil.assertToken(tokenStream, TokenType.IDENTIFIER);
        Token name = tokenStream.token();
        tokenStream.nextToken();
        AssertUtil.assertToken(tokenStream, TokenType.ASSIGN);
        tokenStream.nextToken();
        BaseNode exp = parseExpression();
        return new VariableDefineNode(TokenUtil.buildNameNode(name), exp, varToken.location);
    }

    public BaseNode parseFunDefine() {
        Token funToken = tokenStream.token();
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
        BaseNode block = parseBlock(false);
        return new ClosureDefineNode(name, param, block, funToken.location);
    }

    public BaseNode parseBlock(boolean isLoop) {

        Token lToken = tokenStream.token();

        AssertUtil.assertToken(lToken, TokenType.LEFT_BRACE);
        tokenStream.nextToken();
        List<BaseNode> blocks = new ArrayList<BaseNode>();
        while (tokenStream.token().type != TokenType.EOF
                && tokenStream.token().type != TokenType.RIGHT_BRACE) {
            blocks.add(parseStatement(false));
        }
        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_BRACE);
        tokenStream.nextToken();
        if (isLoop) {
            return new LoopBlockNode(blocks, lToken.location);
        } else {
            return new BlockNode(blocks, lToken.location);
        }
    }

    public BaseNode parseUnaryExpression() {
        Token token = tokenStream.token();
        tokenStream.nextToken();
        if (token.type == TokenType.ADD || token.type == TokenType.SUB) {
            return new UnaryOperationNode(token.type, parsePrimaryExpression(),
                    token.type == TokenType.ADD ? new UnaryAddOperation() : new UnarySubOperation(), token.location);
        } else if (token.type == TokenType.NOT) {
            return new LogicBinaryOperationNode(TokenType.NOT, parsePrimaryExpression(), null,
                    OperationDefine.findLogicOperation(TokenType.NOT), token.location);
        }
        throw SyntaxException.withSyntax("???????????????????????????", token);
    }

    public BaseNode parseCallLink() {
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
        CallLinkNode callLinkNode = new CallLinkNode(firstNode, calls, firstNode.getLocation());
        if (tokenStream.token().type == TokenType.ASSIGN) {
            if (firstNode instanceof OutNameNode) {
                throw SyntaxException.withSyntax("?????????????????????????????????: $" + ((OutNameNode) firstNode).name.name);
            }
            return parseCallLinkAssignNode(callLinkNode);
        }

        return callLinkNode;
    }

    public BaseNode parseDot() {
        Token token = tokenStream.token();
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

            DotNode dotNode = new DotNode(TokenType.CALL, name.name, param, token.location);
            dotNode.putAssignOperation(OperationDefine.findAssignOperation(TokenType.DOT));
            return dotNode;
        } else {
            DotNode dotNode = new DotNode(TokenType.IDENTIFIER, name.name, null, token.location);
            dotNode.putAssignOperation(OperationDefine.findAssignOperation(TokenType.DOT));
            return dotNode;
        }
    }

    public BaseNode parseIndex() {
        Token lToken = tokenStream.token();
        AssertUtil.assertToken(lToken, TokenType.LEFT_SQUARE);
        tokenStream.nextToken();
        BaseNode index = parsePrimaryExpression();
        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_SQUARE);
        tokenStream.nextToken();
        IndexNode indexNode = new IndexNode(index, lToken.location);
        indexNode.putOperation(OperationDefine.findOperation(TokenType.INDEX));
        indexNode.putAssignOperation(OperationDefine.findAssignOperation(TokenType.INDEX));
        return indexNode;
    }

    public BaseNode parseCall() {
        Token lToken = tokenStream.token();
        AssertUtil.assertToken(tokenStream, TokenType.LEFT_PAREN);
        tokenStream.nextToken();
        if (tokenStream.token().type == TokenType.RIGHT_PAREN) {
            tokenStream.nextToken();
            CallNode callNode = new CallNode(Collections.<BaseNode>emptyList(), lToken.location);
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

        CallNode callNode = new CallNode(params, lToken.location);
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
            return new IntegerNode(Integer.parseInt(token.name), token.location);
        }
        if (token.type == TokenType.DOUBLE) {
            return new DoubleNode(Double.parseDouble(token.name), token.location);
        }
        throw SyntaxException.withSyntax("??????????????????", token);
    }

    public BaseNode parseString() {
        AssertUtil.assertToken(tokenStream, TokenType.STRING);
        Token token = tokenStream.token();
        tokenStream.nextToken();
        return new StringNode(token.name, token.location);
    }

    public BaseNode parseBool() {
        Token token = tokenStream.token();
        if (token.type != TokenType.KEY_FALSE && token.type != TokenType.KEY_TRUE) {
            throw SyntaxException.withSyntax("????????????bool", token);
        }
        tokenStream.nextToken();
        return new BoolNode(Boolean.parseBoolean(token.name), token.location);
    }

    public BaseNode parseNull() {
        AssertUtil.assertToken(tokenStream, TokenType.KEY_NULL);
        Token token = tokenStream.token();
        tokenStream.nextToken();
        return new NullNode(token.location);
    }

    public BaseNode parseContinue() {
        AssertUtil.assertToken(tokenStream, TokenType.KEY_CONTINUE);
        Token token = tokenStream.token();
        tokenStream.nextToken();
        return new ContinueNode(token.location);
    }

    public BaseNode parseBreak() {
        AssertUtil.assertToken(tokenStream, TokenType.KEY_BREAK);
        Token token = tokenStream.token();
        tokenStream.nextToken();
        return new BreakNode(token.location);
    }

    public BaseNode parseReturn() {
        AssertUtil.assertToken(tokenStream, TokenType.KEY_RETURN);
        Token returnToken = tokenStream.token();
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
        return new ReturnNode(param, returnToken.location);
    }

    public BaseNode parseArray() {
        AssertUtil.assertToken(tokenStream, TokenType.LEFT_SQUARE);
        Token lToken = tokenStream.token();
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
        return new ArrayNode(arrValues, lToken.location);
    }

    public BaseNode parseTypeOfNode() {
        AssertUtil.assertToken(tokenStream, TokenType.KEY_TYPEOF);
        Token typeOfToken = tokenStream.token();
        tokenStream.nextToken();
        AssertUtil.assertToken(tokenStream, TokenType.LEFT_PAREN);
        tokenStream.nextToken();
        BaseNode exp = parseExpression();
        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_PAREN);
        tokenStream.nextToken();
        return new TypeOfNode(exp, typeOfToken.location);
    }

    public BaseNode parseRsonNode() {
        AssertUtil.assertToken(tokenStream, TokenType.LEFT_BRACE);
        Token lToken = tokenStream.token();
        tokenStream.nextToken();
        Map<String, BaseNode> properties = new HashMap<String, BaseNode>();
        if (tokenStream.token().type != TokenType.RIGHT_BRACE) {
            if (tokenStream.token().type != TokenType.IDENTIFIER
                    && tokenStream.token().type != TokenType.STRING) {
                throw SyntaxException.withSyntax("?????????key", tokenStream.token());
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
                throw SyntaxException.withSyntax("?????????key", tokenStream.token());
            }
            Token name = tokenStream.token();
            tokenStream.nextToken();
            AssertUtil.assertToken(tokenStream, TokenType.COLON);
            tokenStream.nextToken();
            properties.put(name.name, parseExpression());
        }
        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_BRACE);
        tokenStream.nextToken();
        return new RsonNode(properties, lToken.location);
    }

    public BaseNode parseThis() {
        AssertUtil.assertToken(tokenStream, TokenType.KEY_THIS);
        Token thisToken = tokenStream.token();
        tokenStream.nextToken();
        return new ThisNode(thisToken.location);
    }

    public BaseNode parseImport() {
        AssertUtil.assertToken(tokenStream, TokenType.KEY_IMPORT);
        Token importToken = tokenStream.token();
        tokenStream.nextToken();
        AssertUtil.assertToken(tokenStream, TokenType.STRING);
        String path = tokenStream.token().name;
        Token aliasToken = tokenStream.nextToken();
        AssertUtil.assertToken(tokenStream, TokenType.IDENTIFIER);
        tokenStream.nextToken();
        AssertUtil.assertToken(tokenStream, TokenType.SEMICOLON);
        tokenStream.nextToken();
        return new ImportNode(path, aliasToken.name, importToken.location);
    }

    public BaseNode parseRuleBlock() {
        AssertUtil.assertToken(tokenStream, TokenType.KEY_RULE);
        Token ruleToken = tokenStream.token();
        tokenStream.nextToken();

        AssertUtil.assertToken(tokenStream, TokenType.STRING);
        Token nameToken = tokenStream.token();
        tokenStream.nextToken();

        BaseNode blockNode = parseBlock(false);
        return new RuleStatementNode(
                new StringNode(nameToken.name, nameToken.location),
                (BlockNode) blockNode,
                ruleToken.location);
    }

}
