package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.factor.BoolNode;
import com.kamijoucen.ruler.component.TokenStream;
import com.kamijoucen.ruler.component.AtomParser;
import com.kamijoucen.ruler.component.AtomParserManager;
import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.token.TokenType;

/**
 * 布尔字面量解析器
 */
public class BoolParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.KEY_TRUE ||
               tokenStream.token().type == TokenType.KEY_FALSE;
    }

    @Override
    public BaseNode parse(AtomParserManager manager) {
        TokenStream tokenStream = manager.getTokenStream();
        Token token = tokenStream.token();

        if (token.type != TokenType.KEY_TRUE && token.type != TokenType.KEY_FALSE) {
            throw new IllegalStateException("expected boolean value");
        }

        boolean value = token.type == TokenType.KEY_TRUE;
        tokenStream.nextToken();

        return new BoolNode(value, token.location);
    }
}