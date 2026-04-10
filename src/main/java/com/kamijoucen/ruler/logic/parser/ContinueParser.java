package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.factor.ContinueNode;
import com.kamijoucen.ruler.component.TokenStream;
import com.kamijoucen.ruler.component.AtomParser;
import com.kamijoucen.ruler.component.AtomParserManager;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.token.TokenType;
import com.kamijoucen.ruler.logic.util.AssertUtil;

/**
 * continue语句解析器
 */
public class ContinueParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.KEY_CONTINUE;
    }

    @Override
    public BaseNode parse(AtomParserManager manager) {
        TokenStream tokenStream = manager.getTokenStream();

        if (!manager.isInLoop()) {
            throw SyntaxException.withSyntax("continue语句只能在循环内使用", tokenStream.token());
        }

        AssertUtil.assertToken(tokenStream, TokenType.KEY_CONTINUE);
        Token token = tokenStream.token();
        tokenStream.nextToken();

        return new ContinueNode(token.location);
    }
}