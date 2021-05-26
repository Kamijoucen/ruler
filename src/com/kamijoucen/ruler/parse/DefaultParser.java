package com.kamijoucen.ruler.parse;

import com.kamijoucen.ruler.ast.*;
import com.kamijoucen.ruler.ast.statement.*;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.runtime.BinaryDefine;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class DefaultParser implements Parser {

    private Lexical lexical;

    private List<BaseAST> astList;

    public DefaultParser(Lexical lexical) {
        this.lexical = lexical;
        this.astList = new ArrayList<BaseAST>();
    }

    @Override
    public List<BaseAST> parse() {

        lexical.nextToken();

        while (lexical.getToken().type != TokenType.EOF) {
            astList.add(parseStatement());
        }
        return astList;
    }

    public BaseAST parseStatement() {
        Token token = lexical.getToken();

        BaseAST statement = null;

        boolean isNeedSemicolon = false;
        switch (token.type) {
            case IDENTIFIER:
            case OUT_IDENTIFIER:
                statement = parseIdentifier(true);
                isNeedSemicolon = true;
                break;
            case KEY_RETURN:
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


    public BaseAST parseExpression() {

        Stack<BaseAST> valStack = new Stack<BaseAST>();

        Stack<TokenType> opStack = new Stack<TokenType>();

        valStack.push(parsePrimaryExpression()); // first exp

        while (lexical.getToken().type != TokenType.EOF
                && lexical.getToken().type != TokenType.SEMICOLON
                && lexical.getToken().type != TokenType.RIGHT_PAREN
                && lexical.getToken().type != TokenType.LEFT_BRACE
                && lexical.getToken().type != TokenType.COMMA) {

            Token op = lexical.getToken();

            int curPrecedence = BinaryDefine.findPrecedence(op.type);
            if (curPrecedence == -1) {
                throw SyntaxException.withSyntax("不支持的的二元操作符: " + op.type);
            }
            lexical.nextToken();

            BaseAST rls = parsePrimaryExpression();

            if (opStack.size() != 0) {
                TokenType peek = opStack.peek();
                int peekPrecedence = BinaryDefine.findPrecedence(peek);
                if (peekPrecedence == -1) {
                    throw SyntaxException.withSyntax("不支持的的二元操作符:" + peek);
                }
                if (curPrecedence <= peekPrecedence) {
                    BaseAST exp1 = valStack.pop();
                    BaseAST exp2 = valStack.pop();

                    TokenType binOp = opStack.pop();

                    valStack.push(new BinaryOperationAST(binOp, exp2, exp1));
                }
            }
            opStack.push(op.type);
            valStack.push(rls);
        }
        while (opStack.size() != 0) {
            BaseAST exp1 = valStack.pop();
            BaseAST exp2 = valStack.pop();

            TokenType binOp = opStack.pop();

            valStack.push(new BinaryOperationAST(binOp, exp2, exp1));
        }
        return valStack.pop();
    }

    public BaseAST parsePrimaryExpression() {

        Token token = lexical.getToken();

        switch (token.type) {
            case IDENTIFIER:
            case OUT_IDENTIFIER:
                return parseIdentifier(false);
            case ADD:
            case SUB:
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
        }
        throw SyntaxException.withSyntax("未知的表达式起始", token);
    }

    public BaseAST parseWhileStatement() {

        Assert.assertToken(lexical, TokenType.KEY_WHILE);

        lexical.nextToken();

        BaseAST condition = parseExpression();

        BaseAST blockAST = null;

        if (lexical.getToken().type == TokenType.LEFT_BRACE) {
            blockAST = parseBlock();
        } else {
            blockAST = new BlockAST(Collections.singletonList(parseStatement()));
        }

        return new WhileStatementAST(condition, blockAST);
    }

    public BaseAST parseIfStatement() {

        Assert.assertToken(lexical, TokenType.KEY_IF);

        lexical.nextToken();

        BaseAST condition = parseExpression();

        BaseAST thenBlock = null;
        if (lexical.getToken().type == TokenType.LEFT_BRACE) {
            thenBlock = parseBlock();
        } else {
            thenBlock = new BlockAST(Collections.singletonList(parseStatement()));
        }

        BaseAST elseBlock = null;
        if (lexical.getToken().type == TokenType.KEY_ELSE) {
            Token token = lexical.nextToken();
            if (token.type == TokenType.LEFT_BRACE) {
                elseBlock = parseBlock();
            } else {
                elseBlock = new BlockAST(Collections.singletonList(parseStatement()));
            }
        }
        return new IfStatementAST(condition, thenBlock, elseBlock);
    }


    public BaseAST parseBlock() {

        Assert.assertToken(lexical, TokenType.LEFT_BRACE);

        lexical.nextToken();

        List<BaseAST> blocks = new ArrayList<BaseAST>();

        while (lexical.getToken().type != TokenType.EOF
                && lexical.getToken().type != TokenType.RIGHT_BRACE) {
            blocks.add(parseStatement());
        }

        Assert.assertToken(lexical, TokenType.RIGHT_BRACE);

        lexical.nextToken();

        return new BlockAST(blocks);
    }

    public BaseAST parseUnaryExpression() {
        Token token = lexical.getToken();
        if (token.type != TokenType.ADD
                && token.type != TokenType.SUB) {
            throw SyntaxException.withSyntax("不支持的单目运算符", token);
        }

        lexical.nextToken();
        return new UnaryOperationAST(token.type, parsePrimaryExpression());
    }

    public BaseAST parseIdentifier(boolean isStatement) {

        Token token = lexical.getToken();

        if (token.type != TokenType.OUT_IDENTIFIER
                && token.type != TokenType.IDENTIFIER) {
            throw SyntaxException.withSyntax("不支持的单目运算符", token);
        }
        Token nextToken = lexical.nextToken();

        switch (nextToken.type) {
            case DOT:
                throw Assert.todo("调用尚未实现");
            case LEFT_PAREN:
                return parseCall(token);
            case ASSIGN:
                if (!isStatement) {
                    throw SyntaxException.withSyntax("赋值语句不能出现在表达式内");
                }
                return parseAssign(token);
            default:
                if (isStatement) {
                    throw SyntaxException.withSyntax("不是一个语句", token);
                }
                return new NameAST(token, token.type == TokenType.OUT_IDENTIFIER);
        }
    }

    public BaseAST parseCall(Token identifier) {

        Assert.assertToken(lexical, TokenType.LEFT_PAREN);

        NameAST nameAST = new NameAST(identifier,
                identifier.type == TokenType.OUT_IDENTIFIER);

        lexical.nextToken();

        if (lexical.getToken().type == TokenType.RIGHT_PAREN) {
            lexical.nextToken();
            return new CallAST(nameAST, Collections.<BaseAST>emptyList());
        }

        BaseAST param1 = parseExpression();

        List<BaseAST> params = new ArrayList<BaseAST>();
        params.add(param1);

        while (lexical.getToken().type != TokenType.EOF
                && lexical.getToken().type != TokenType.RIGHT_PAREN) {
            Assert.assertToken(lexical, TokenType.COMMA);
            lexical.nextToken();
            params.add(parseExpression());
        }

        Assert.assertToken(lexical, TokenType.RIGHT_PAREN);

        lexical.nextToken();

        return new CallAST(nameAST, params);
    }

    public BaseAST parseAssign(Token identifier) {

        Assert.assertToken(lexical, TokenType.ASSIGN);

        lexical.nextToken();

        NameAST nameAST = new NameAST(identifier, identifier.type == TokenType.OUT_IDENTIFIER);

        BaseAST expression = parseExpression();

        return new AssignAST(nameAST, expression);
    }

    public BaseAST parseParen() {

        Assert.assertToken(lexical, TokenType.LEFT_PAREN);

        lexical.nextToken();

        BaseAST ast = parseExpression();

        Assert.assertToken(lexical, TokenType.RIGHT_PAREN);

        lexical.nextToken();

        return ast;
    }

    public BaseAST parseNumber() {

        Token token = lexical.getToken();

        lexical.nextToken();

        if (token.type == TokenType.INTEGER) {
            return new IntegerAST(Integer.parseInt(token.name));
        }

        if (token.type == TokenType.DOUBLE) {
            return new DoubleAST(Double.parseDouble(token.name));
        }

        throw SyntaxException.withSyntax("需要一个数字", token);
    }

    public BaseAST parseString() {

        Assert.assertToken(lexical, TokenType.STRING);

        Token token = lexical.getToken();

        lexical.nextToken();

        return new StringAST(token.name);
    }

    public BaseAST parseBool() {

        Token token = lexical.getToken();

        if (token.type != TokenType.KEY_FALSE
                && token.type != TokenType.KEY_TRUE) {
            throw SyntaxException.withSyntax("需要一个bool", token);
        }
        lexical.nextToken();

        return new BoolAST(Boolean.parseBoolean(token.name));
    }

    public BaseAST parseContinueAST() {

        Assert.assertToken(lexical, TokenType.KEY_CONTINUE);

        lexical.nextToken();

        return new ContinueAST();
    }

    public BaseAST parseBreak() {

        Assert.assertToken(lexical, TokenType.KEY_BREAK);

        lexical.nextToken();

        return new BreakAST();

    }

}
