package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.types.parser.ParseState;
import com.kamijoucen.ruler.types.parser.TokenStream;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.ArrayNode;

import com.kamijoucen.ruler.types.token.Token;
import com.kamijoucen.ruler.types.token.TokenType;
import com.kamijoucen.ruler.logic.util.AssertUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 数组字面量解析器
 */
public class ArrayParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.LEFT_SQUARE;
    }

    @Override
    public BaseNode parse(ParseState state) {
        TokenStream tokenStream = state.tokens;

        AssertUtil.assertToken(tokenStream, TokenType.LEFT_SQUARE);
        Token lToken = tokenStream.token();
        tokenStream.nextToken();

        List<BaseNode> arrValues = new ArrayList<>();
        if (tokenStream.token().type != TokenType.RIGHT_SQUARE) {
            arrValues.add(Parser.parseExpression(state));
        }

        while (tokenStream.token().type != TokenType.RIGHT_SQUARE) {
            AssertUtil.assertToken(tokenStream, TokenType.COMMA);
            tokenStream.nextToken();
            arrValues.add(Parser.parseExpression(state));
        }

        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_SQUARE);
        tokenStream.nextToken();

        return new ArrayNode(arrValues, lToken.location);
    }
}
