package com.kamijoucen.ruler.logic.parser;

import java.util.ArrayList;
import java.util.List;
import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.factor.ReturnNode;
import com.kamijoucen.ruler.component.TokenStream;
import com.kamijoucen.ruler.component.AtomParser;
import com.kamijoucen.ruler.component.AtomParserManager;
import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.token.TokenType;
import com.kamijoucen.ruler.logic.util.AssertUtil;

/**
 * return语句解析器
 */
public class ReturnParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.KEY_RETURN;
    }

    @Override
    public BaseNode parse(AtomParserManager manager) {
        TokenStream tokenStream = manager.getTokenStream();

        AssertUtil.assertToken(tokenStream, TokenType.KEY_RETURN);
        Token returnToken = tokenStream.token();
        tokenStream.nextToken();

        List<BaseNode> param = new ArrayList<>();
        if (tokenStream.token().type != TokenType.SEMICOLON) {
            param.add(manager.parseExpression());
        }

        while (tokenStream.token().type != TokenType.SEMICOLON) {
            AssertUtil.assertToken(tokenStream, TokenType.COMMA);
            tokenStream.nextToken();
            param.add(manager.parseExpression());
        }

        return new ReturnNode(param, returnToken.location);
    }
}