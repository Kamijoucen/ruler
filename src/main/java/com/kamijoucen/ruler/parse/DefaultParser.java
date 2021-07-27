package com.kamijoucen.ruler.parse;

import com.kamijoucen.ruler.ast.*;
import com.kamijoucen.ruler.ast.op.CallNode;
import com.kamijoucen.ruler.ast.op.DotNode;
import com.kamijoucen.ruler.ast.op.IndexNode;
import com.kamijoucen.ruler.ast.op.OperationNode;
import com.kamijoucen.ruler.ast.statement.*;
import com.kamijoucen.ruler.common.CollectionUtil;
import com.kamijoucen.ruler.common.RStack;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.OperationDefine;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.util.Assert;
import com.kamijoucen.ruler.util.TokenUtil;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.*;

public class DefaultParser implements Parser {

    private Lexical lexical;

    private List<BaseNode> statements;

    public DefaultParser(Lexical lexical) {
        this.lexical = lexical;
        this.statements = new ArrayList<BaseNode>();
    }

    @Override
    public List<BaseNode> parse() {

        lexical.nextToken();

        while (lexical.getToken().type != TokenType.EOF) {
            statements.add(parseStatement());
        }
        return statements;
    }

    public BaseNode parseStatement() {
        Token token = lexical.getToken();

        BaseNode statement = null;

        boolean isNeedSemicolon = false;
        switch (token.type) {
            case IDENTIFIER:
            case OUT_IDENTIFIER:
                statement = parseIdentifier(true);
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
            case KEY_LIST:
                break;
            case KEY_MAP:
                break;
        }
        if (statement == null) {
            throw SyntaxException.withSyntax("错误的语句");
        }
        if (isNeedSemicolon) {
            Assert.assertToken(lexical, TokenType.SEMICOLON);
            lexical.nextToken();
        }
        return statement;
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
                return parseIdentifier(false);
            case ADD:
            case SUB:
            case NOT:
                return parseUnaryExpression();
            case INTEGER:
            case DOUBLE:
                return parseNumber();
            case STRING:
                return parseString();
            case LEFT_PAREN:
                return parseParen();
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

        Assert.assertToken(lexical, TokenType.KEY_WHILE);

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

        Assert.assertToken(lexical, TokenType.KEY_IF);

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
        Assert.assertToken(lexical, TokenType.KEY_VAR);

        lexical.nextToken();

        Token name = lexical.getToken();

        if (name.type != TokenType.OUT_IDENTIFIER && name.type != TokenType.IDENTIFIER) {
            throw SyntaxException.withSyntax("不支持的单目运算符", name);
        }

        lexical.nextToken();

        Assert.assertToken(lexical, TokenType.ASSIGN);

        lexical.nextToken();

        BaseNode exp = parseExpression();

        return new VariableDefineNode(new NameNode(name, name.type == TokenType.OUT_IDENTIFIER), exp);
    }

    public BaseNode parseFunDefine() {

        // eat fun
        Assert.assertToken(lexical, TokenType.KEY_FUN);

        lexical.nextToken();

        String name = null;
        if (lexical.getToken().type == TokenType.IDENTIFIER) {
            name = lexical.getToken().name;
            lexical.nextToken();
        }

        // eat (
        Assert.assertToken(lexical, TokenType.LEFT_PAREN);
        lexical.nextToken();

        List<BaseNode> param = new ArrayList<BaseNode>();

        if (lexical.getToken().type != TokenType.RIGHT_PAREN) {
            Assert.assertToken(lexical, TokenType.IDENTIFIER);
            Token token = lexical.getToken();
            param.add(new NameNode(token, token.type == TokenType.OUT_IDENTIFIER));

            lexical.nextToken();
        }

        while (lexical.getToken().type != TokenType.RIGHT_PAREN) {
            Assert.assertToken(lexical, TokenType.COMMA);
            lexical.nextToken();

            Assert.assertToken(lexical, TokenType.IDENTIFIER);
            Token token = lexical.getToken();
            param.add(new NameNode(token, token.type == TokenType.OUT_IDENTIFIER));

            lexical.nextToken();
        }

        // eat )
        Assert.assertToken(lexical, TokenType.RIGHT_PAREN);
        lexical.nextToken();
        BaseNode block = parseBlock();

        return new ClosureDefineNode(name, param, block);
    }

    public BaseNode parseBlock() {

        Assert.assertToken(lexical, TokenType.LEFT_BRACE);

        lexical.nextToken();

        List<BaseNode> blocks = new ArrayList<BaseNode>();

        while (lexical.getToken().type != TokenType.EOF && lexical.getToken().type != TokenType.RIGHT_BRACE) {
            blocks.add(parseStatement());
        }

        Assert.assertToken(lexical, TokenType.RIGHT_BRACE);

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

    public BaseNode parseIdentifier(boolean isStatement) {

        Token token = lexical.getToken();

        if (token.type != TokenType.OUT_IDENTIFIER && token.type != TokenType.IDENTIFIER) {
            throw SyntaxException.withSyntax("不支持的标识符", token);
        }
        Token nextToken = lexical.nextToken();

        switch (nextToken.type) {
            case DOT:
            case LEFT_PAREN:
            case LEFT_SQUARE:
                return parseCallLink(token);
            case ASSIGN:
                if (!isStatement) {
                    throw SyntaxException.withSyntax("赋值语句不能出现在表达式内");
                }
                return parseAssign(token);
            default:
                if (isStatement) {
                    throw SyntaxException.withSyntax("不是一个语句", token);
                }
                return new NameNode(token, token.type == TokenType.OUT_IDENTIFIER);
        }
    }

    public BaseNode parseCallLink(Token identifier) {

        List<OperationNode> calls = new ArrayList<OperationNode>();

        while (lexical.getToken().type == TokenType.LEFT_PAREN || lexical.getToken().type == TokenType.LEFT_SQUARE
                || lexical.getToken().type == TokenType.DOT || lexical.getToken().type == TokenType.ASSIGN) {
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
                case ASSIGN:
                    OperationNode lastNode = CollectionUtil.last(calls);
                    if (lastNode == null || lastNode.getOperationType() != TokenType.INDEX) {
                        throw SyntaxException.withSyntax("赋值运算左侧的表达式无效");
                    }
                    NameNode name = new NameNode(identifier, identifier.type == TokenType.OUT_IDENTIFIER);
                    return parseArrayAssign(name, calls);
            }
        }
        return new CallLinkedNode(new NameNode(identifier, identifier.type == TokenType.OUT_IDENTIFIER), calls);
    }

    public BaseNode parseDot() {

        Assert.assertToken(lexical, TokenType.DOT);

        lexical.nextToken();

        Assert.assertToken(lexical, TokenType.IDENTIFIER);

        Token name = lexical.getToken();

        lexical.nextToken();

        if (lexical.getToken().type == TokenType.LEFT_PAREN) {

            lexical.nextToken();

            List<BaseNode> param = new ArrayList<BaseNode>();

            if (lexical.getToken().type != TokenType.RIGHT_PAREN) {
                param.add(parseExpression());
            }

            while (lexical.getToken().type != TokenType.RIGHT_PAREN) {
                Assert.assertToken(lexical, TokenType.COMMA);
                param.add(parseExpression());
            }

            Assert.assertToken(lexical, TokenType.RIGHT_PAREN);
            lexical.nextToken();

            return new DotNode(TokenType.CALL, name.name, param);
        } else {
            return new DotNode(TokenType.IDENTIFIER, name.name, null);
        }
    }

    public BaseNode parseIndex() {

        Assert.assertToken(lexical, TokenType.LEFT_SQUARE);

        lexical.nextToken();

        BaseNode index = parsePrimaryExpression();

        Assert.assertToken(lexical, TokenType.RIGHT_SQUARE);

        lexical.nextToken();

        IndexNode indexNode = new IndexNode(index);
        indexNode.putOperation(OperationDefine.findOperation(TokenType.INDEX));

        return indexNode;
    }

    public BaseNode parseCall() {

        Assert.assertToken(lexical, TokenType.LEFT_PAREN);

        lexical.nextToken();

        if (lexical.getToken().type == TokenType.RIGHT_PAREN) {
            lexical.nextToken();
            return new CallNode(Collections.<BaseNode>emptyList());
        }

        BaseNode param1 = parseExpression();

        List<BaseNode> params = new ArrayList<BaseNode>();
        params.add(param1);

        while (lexical.getToken().type != TokenType.EOF && lexical.getToken().type != TokenType.RIGHT_PAREN) {
            Assert.assertToken(lexical, TokenType.COMMA);
            lexical.nextToken();
            params.add(parseExpression());
        }

        Assert.assertToken(lexical, TokenType.RIGHT_PAREN);

        lexical.nextToken();

        CallNode callNode = new CallNode(params);
        callNode.putOperation(OperationDefine.findOperation(TokenType.CALL));
        
        return callNode;
    }

    public BaseNode parseArrayAssign(NameNode name, List<OperationNode> callList) {

        Assert.assertToken(lexical, TokenType.ASSIGN);

        lexical.nextToken();

        OperationNode indexNode = CollectionUtil.last(callList);

        if (indexNode == null || indexNode.getOperationType() != TokenType.INDEX) {
            throw SyntaxException.withSyntax("对数组元素赋值需要指定下标");
        }
        CollectionUtil.removeLast(callList);

        CallLinkedNode calls = new CallLinkedNode(name, callList);

        BaseNode rls = parseExpression();

        return new ArrayAssignNode(calls, (IndexNode) indexNode, rls);
    }

    public BaseNode parseAssign(Token identifier) {

        if (identifier.type == TokenType.OUT_IDENTIFIER) {
            throw SyntaxException.withSyntax("外部变量不允许进行赋值操作", identifier);
        }

        Assert.assertToken(lexical, TokenType.ASSIGN);

        lexical.nextToken();

        NameNode nameAST = new NameNode(identifier, false);

        BaseNode expression = parseExpression();

        return new AssignNode(nameAST, expression);
    }

    public BaseNode parseParen() {

        Assert.assertToken(lexical, TokenType.LEFT_PAREN);

        lexical.nextToken();

        BaseNode ast = parseExpression();

        Assert.assertToken(lexical, TokenType.RIGHT_PAREN);

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

        Assert.assertToken(lexical, TokenType.STRING);

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

        Assert.assertToken(lexical, TokenType.KEY_NULL);

        lexical.nextToken();

        return NullNode.NULL_NODE;
    }

    public BaseNode parseContinueAST() {

        Assert.assertToken(lexical, TokenType.KEY_CONTINUE);

        lexical.nextToken();

        return new ContinueNode();
    }

    public BaseNode parseBreak() {

        Assert.assertToken(lexical, TokenType.KEY_BREAK);

        lexical.nextToken();

        return new BreakNode();
    }

    public BaseNode parseReturn() {

        Assert.assertToken(lexical, TokenType.KEY_RETURN);

        lexical.nextToken();

        List<BaseNode> param = new ArrayList<BaseNode>();

        if (lexical.getToken().type != TokenType.SEMICOLON) {
            param.add(parseExpression());
        }

        while (lexical.getToken().type != TokenType.SEMICOLON) {
            Assert.assertToken(lexical, TokenType.COMMA);
            lexical.nextToken();
            param.add(parseExpression());
        }

        return new ReturnNode(param);
    }

    public BaseNode parseArray() {

        Assert.assertToken(lexical, TokenType.LEFT_SQUARE);

        lexical.nextToken();

        List<BaseNode> arrValues = new ArrayList<BaseNode>();

        if (lexical.getToken().type != TokenType.RIGHT_SQUARE) {
            arrValues.add(parseExpression());
        }

        while (lexical.getToken().type != TokenType.RIGHT_SQUARE) {

            Assert.assertToken(lexical, TokenType.COMMA);

            lexical.nextToken();

            arrValues.add(parseExpression());
        }

        Assert.assertToken(lexical, TokenType.RIGHT_SQUARE);

        lexical.nextToken();

        return new ArrayNode(arrValues);
    }

    public BaseNode parseRsonNode() {

        Assert.assertToken(lexical, TokenType.LEFT_BRACE);

        lexical.nextToken();

        Map<String, BaseNode> properties = new HashMap<String, BaseNode>();

        if (lexical.getToken().type != TokenType.RIGHT_BRACE) {

            if (lexical.getToken().type != TokenType.IDENTIFIER && lexical.getToken().type != TokenType.STRING) {
                throw SyntaxException.withSyntax("无效的key", lexical.getToken());
            }

            Token name = lexical.getToken();
            lexical.nextToken();

            Assert.assertToken(lexical, TokenType.COLON);
            lexical.nextToken();

            BaseNode value = parseExpression();

            properties.put(name.name, value);

        }

        while (lexical.getToken().type != TokenType.RIGHT_BRACE) {

            Assert.assertToken(lexical, TokenType.COMMA);

            lexical.nextToken();

            if (lexical.getToken().type == TokenType.RIGHT_BRACE) {
                break;
            }

            if (lexical.getToken().type != TokenType.IDENTIFIER && lexical.getToken().type != TokenType.STRING) {
                throw SyntaxException.withSyntax("无效的key", lexical.getToken());
            }

            Token name = lexical.getToken();
            lexical.nextToken();

            Assert.assertToken(lexical, TokenType.COLON);
            lexical.nextToken();

            BaseNode value = parseExpression();

            properties.put(name.name, value);
        }

        Assert.assertToken(lexical, TokenType.RIGHT_BRACE);

        lexical.nextToken();

        return new RsonNode(properties);
    }

}