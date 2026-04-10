package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.component.TokenStream;
import com.kamijoucen.ruler.component.AtomParser;
import com.kamijoucen.ruler.component.AtomParserManager;
import com.kamijoucen.ruler.domain.token.TokenType;
import com.kamijoucen.ruler.logic.util.AssertUtil;

/**
 * 括号表达式解析器
 */
public class ParenParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.LEFT_PAREN;
    }

    @Override
    public BaseNode parse(AtomParserManager manager) {
        TokenStream tokenStream = manager.getTokenStream();

        AssertUtil.assertToken(tokenStream, TokenType.LEFT_PAREN);
        tokenStream.nextToken();

        BaseNode ast = manager.parseExpression();

        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_PAREN);
        tokenStream.nextToken();

        return ast;
    }
}