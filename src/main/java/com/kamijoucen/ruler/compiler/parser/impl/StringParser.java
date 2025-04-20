package com.kamijoucen.ruler.compiler.parser.impl;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.factor.StringNode;
import com.kamijoucen.ruler.compiler.TokenStream;
import com.kamijoucen.ruler.compiler.parser.AtomParser;
import com.kamijoucen.ruler.compiler.parser.AtomParserManager;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.util.AssertUtil;

/**
 * 字符串字面量解析器
 */
public class StringParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.STRING;
    }

    @Override
    public BaseNode parse(AtomParserManager manager) {
        TokenStream tokenStream = manager.getTokenStream();

        AssertUtil.assertToken(tokenStream, TokenType.STRING);
        Token token = tokenStream.token();
        tokenStream.nextToken();

        return new StringNode(token.name, token.location);
    }
}