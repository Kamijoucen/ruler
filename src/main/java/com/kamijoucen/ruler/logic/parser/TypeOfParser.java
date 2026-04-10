package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.factor.TypeOfNode;
import com.kamijoucen.ruler.component.TokenStream;
import com.kamijoucen.ruler.component.AtomParser;
import com.kamijoucen.ruler.component.AtomParserManager;
import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.token.TokenType;
import com.kamijoucen.ruler.logic.util.AssertUtil;

/**
 * typeof操作解析器
 */
public class TypeOfParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.KEY_TYPEOF;
    }

    @Override
    public BaseNode parse(AtomParserManager manager) {
        TokenStream tokenStream = manager.getTokenStream();

        AssertUtil.assertToken(tokenStream, TokenType.KEY_TYPEOF);
        Token typeOfToken = tokenStream.token();
        tokenStream.nextToken();

        AssertUtil.assertToken(tokenStream, TokenType.LEFT_PAREN);
        tokenStream.nextToken();

        BaseNode exp = manager.parseExpression();

        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_PAREN);
        tokenStream.nextToken();

        return new TypeOfNode(exp, typeOfToken.location);
    }
}