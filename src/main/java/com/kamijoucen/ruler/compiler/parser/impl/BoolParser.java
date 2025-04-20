package com.kamijoucen.ruler.compiler.parser.impl;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.factor.BoolNode;
import com.kamijoucen.ruler.compiler.TokenStream;
import com.kamijoucen.ruler.compiler.parser.AtomParser;
import com.kamijoucen.ruler.compiler.parser.AtomParserManager;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;

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
            throw new IllegalStateException("需要boolean值");
        }

        boolean value = token.type == TokenType.KEY_TRUE;
        tokenStream.nextToken();

        return new BoolNode(value, token.location);
    }
}