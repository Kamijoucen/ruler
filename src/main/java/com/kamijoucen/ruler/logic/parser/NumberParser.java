package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.types.parser.ParseState;
import com.kamijoucen.ruler.types.parser.TokenStream;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.DoubleNode;
import com.kamijoucen.ruler.types.ast.IntegerNode;

import com.kamijoucen.ruler.types.exception.SyntaxException;
import com.kamijoucen.ruler.types.token.Token;
import com.kamijoucen.ruler.types.token.TokenType;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 数字字面量解析器
 */
public class NumberParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.INTEGER ||
               tokenStream.token().type == TokenType.DOUBLE;
    }

    @Override
    public BaseNode parse(ParseState state) {
        TokenStream tokenStream = state.tokens;
        Token token = tokenStream.token();
        tokenStream.nextToken();

        if (token.type == TokenType.INTEGER) {
            return new IntegerNode(new BigInteger(token.name), token.location);
        } else if (token.type == TokenType.DOUBLE) {
            return new DoubleNode(new BigDecimal(token.name), token.location);
        } else {
            throw new SyntaxException("expected a number\t token=" + token);
        }
    }
}
