package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.factor.RsonNode;
import com.kamijoucen.ruler.component.TokenStream;
import com.kamijoucen.ruler.component.AtomParser;
import com.kamijoucen.ruler.component.AtomParserManager;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.token.TokenType;
import com.kamijoucen.ruler.logic.util.AssertUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * RSON（对象）字面量解析器
 */
public class RsonParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.LEFT_BRACE;
    }

    @Override
    public BaseNode parse(AtomParserManager manager) {
        TokenStream tokenStream = manager.getTokenStream();

        AssertUtil.assertToken(tokenStream, TokenType.LEFT_BRACE);
        Token lToken = tokenStream.token();
        tokenStream.nextToken();

        Map<String, BaseNode> properties = new HashMap<>();
        if (tokenStream.token().type != TokenType.RIGHT_BRACE) {
            if (tokenStream.token().type != TokenType.IDENTIFIER
                    && tokenStream.token().type != TokenType.STRING) {
                throw SyntaxException.withSyntax("无效的key", tokenStream.token());
            }
            Token name = tokenStream.token();
            tokenStream.nextToken();

            AssertUtil.assertToken(tokenStream, TokenType.COLON);
            tokenStream.nextToken();
            properties.put(name.name, manager.parseExpression());
        }

        while (tokenStream.token().type != TokenType.RIGHT_BRACE) {
            AssertUtil.assertToken(tokenStream, TokenType.COMMA);
            tokenStream.nextToken();

            if (tokenStream.token().type == TokenType.RIGHT_BRACE) {
                break;
            }
            if (tokenStream.token().type != TokenType.IDENTIFIER
                    && tokenStream.token().type != TokenType.STRING) {
                throw SyntaxException.withSyntax("无效的key", tokenStream.token());
            }
            Token name = tokenStream.token();
            tokenStream.nextToken();
            AssertUtil.assertToken(tokenStream, TokenType.COLON);
            tokenStream.nextToken();
            properties.put(name.name, manager.parseExpression());
        }

        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_BRACE);
        tokenStream.nextToken();

        return new RsonNode(properties, lToken.location);
    }
}