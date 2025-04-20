package com.kamijoucen.ruler.compiler.parser.impl;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.factor.ArrayNode;
import com.kamijoucen.ruler.compiler.TokenStream;
import com.kamijoucen.ruler.compiler.parser.AtomParser;
import com.kamijoucen.ruler.compiler.parser.AtomParserManager;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.util.AssertUtil;

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
    public BaseNode parse(AtomParserManager manager) {
        TokenStream tokenStream = manager.getTokenStream();

        AssertUtil.assertToken(tokenStream, TokenType.LEFT_SQUARE);
        Token lToken = tokenStream.token();
        tokenStream.nextToken();

        List<BaseNode> arrValues = new ArrayList<>();
        if (tokenStream.token().type != TokenType.RIGHT_SQUARE) {
            arrValues.add(manager.parseExpression());
        }

        while (tokenStream.token().type != TokenType.RIGHT_SQUARE) {
            AssertUtil.assertToken(tokenStream, TokenType.COMMA);
            tokenStream.nextToken();
            arrValues.add(manager.parseExpression());
        }

        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_SQUARE);
        tokenStream.nextToken();

        return new ArrayNode(arrValues, lToken.location);
    }
}