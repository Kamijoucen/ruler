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

        }

        return astList;
    }


    public BaseAST parseExpression() {

        Stack<BaseAST> valStack = new Stack<BaseAST>();

        Stack<TokenType> opStack = new Stack<TokenType>();

        valStack.push(parsePrimaryExpression()); // first exp

        while (lexical.getToken().type != TokenType.EOF
                && lexical.getToken().type != TokenType.SEMICOLON) {

            Token op = lexical.getToken();

            opStack.push(op.type);

            valStack.push(parsePrimaryExpression()); // rhs

            TokenType peek = opStack.peek();
            if (peek == null) {
                // TODO: 2021/4/30
            }
        }

        return null;
    }

    public BaseAST parsePrimaryExpression() {

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
                return new NameAST(token, isOut);
        }
    }

    public BaseAST parseCall(Token identifier) {

        Utils.assertToken(lexical, TokenType.LEFT_PAREN);

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
            Utils.assertToken(lexical, TokenType.COMMA);
            lexical.nextToken();
            params.add(parseExpression());
        }

        return new CallAST(nameAST, params);
    }

    public BaseAST parseAssign(Token identifier) {

        Utils.assertToken(lexical, TokenType.ASSIGN);

        lexical.nextToken();

        NameAST nameAST = new NameAST(identifier, identifier.type == TokenType.OUT_IDENTIFIER);

        BaseAST expression = parseExpression();

        return new AssignAST(nameAST, expression);
    }

    public BaseAST parseParen() {

        Utils.assertToken(lexical, TokenType.LEFT_PAREN);

        lexical.nextToken();

        BaseAST ast = parseExpression();

        Utils.assertToken(lexical, TokenType.RIGHT_PAREN);

        lexical.nextToken();
        return ast;
    }

}
