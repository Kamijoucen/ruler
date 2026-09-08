package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.types.parser.ParseState;
import com.kamijoucen.ruler.types.parser.TokenStream;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.NullNode;

import com.kamijoucen.ruler.types.token.Token;
import com.kamijoucen.ruler.types.token.TokenType;
import com.kamijoucen.ruler.logic.util.AssertUtil;

/**
 * null字面量解析器
 */
public class NullParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.KEY_NULL;
    }

    @Override
    public BaseNode parse(ParseState state) {
        TokenStream tokenStream = state.tokens;

        AssertUtil.assertToken(tokenStream, TokenType.KEY_NULL);
        Token token = tokenStream.token();
        tokenStream.nextToken();

        return new NullNode(token.location);
    }
}
