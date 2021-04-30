package com.kamijoucen.ruler.parse;

import com.kamijoucen.ruler.ast.BaseAST;
import com.kamijoucen.ruler.ast.CallAST;
import com.kamijoucen.ruler.ast.NameAST;
import com.kamijoucen.ruler.ast.AssignAST;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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


        }

        return astList;
    }


    public BaseAST parseExpression() {

        Token token = lexical.getToken();

        switch (token.type) {
            case IDENTIFIER:
                return parseIdentifier(false);
            case OUT_IDENTIFIER:
                return parseIdentifier(true);
            case INTEGER:
                break;
            case DOUBLE:
                break;
            case STRING:
                break;
            case LEFT_PAREN:
                return parseParen();
            case KEY_FALSE:
                break;
            case KEY_TRUE:
                break;
            default:
                break;
        }

        return null;
    }

    public BaseAST parseIdentifier(boolean isOut) {

        Token token = lexical.getToken();

        if (isOut) {
            Utils.assertToken(token, TokenType.OUT_IDENTIFIER);
        } else {
            Utils.assertToken(token, TokenType.IDENTIFIER);
        }

        Token nextToken = lexical.nextToken();

        switch (nextToken.type) {
            case DOT:
                throw Utils.todo("调用尚未实现");
            case LEFT_PAREN:
                return parseCall(token);
            case ASSIGN:
                return parseAssign(token);
            default:
                throw SyntaxException.withSyntax("未知的语法", nextToken);
        }
    }

    private BaseAST parseCall(Token identifier) {

        Utils.assertToken(identifier, TokenType.LEFT_PAREN);

        NameAST nameAST = new NameAST(identifier,
                identifier.type == TokenType.OUT_IDENTIFIER);

        lexical.nextToken();

        if (lexical.getToken().type == TokenType.RIGHT_PAREN) {
            lexical.nextToken();
            return new CallAST(nameAST, Collections.<BaseAST>emptyList());
        }

        BaseAST param1 = parseExpression();
        if (param1 == null) {
            throw SyntaxException.withSyntax("函数调用的参数格式不正确", identifier);
        }

        List<BaseAST> params = new ArrayList<BaseAST>();
        params.add(param1);

        while (lexical.getToken().type != TokenType.EOF
                && lexical.getToken().type != TokenType.RIGHT_PAREN) {
            Utils.assertToken(identifier, TokenType.COMMA);
            lexical.nextToken();
            BaseAST param = parseExpression();
            if (param == null) {
                throw SyntaxException.withSyntax("函数调用的参数格式不正确", identifier);
            }
            params.add(param);
        }

        return new CallAST(nameAST, params);
    }

    private BaseAST parseAssign(Token identifier) {

        Utils.assertToken(identifier, TokenType.ASSIGN);

        lexical.nextToken();

        NameAST nameAST = new NameAST(identifier, identifier.type == TokenType.OUT_IDENTIFIER);

        BaseAST expression = parseExpression();

        if (expression == null) {
            throw SyntaxException.withSyntax("复制语句没有发现表达式", identifier);
        }

        return new AssignAST(nameAST, expression);
    }

    public BaseAST parseParen() {

        Token leftParenToken = lexical.getToken();

        Utils.assertToken(leftParenToken, TokenType.LEFT_PAREN);

        lexical.nextToken();

        BaseAST ast = parseExpression();

        if (ast == null) {
            throw SyntaxException.withSyntax("括号内没有发现表达式", leftParenToken);
        }

        Utils.assertToken(lexical.getToken(), TokenType.RIGHT_PAREN);

        lexical.nextToken();
        return ast;
    }


    public BaseAST parseBinary() {
        return null;
    }


}
