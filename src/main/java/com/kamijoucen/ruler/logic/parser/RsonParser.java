package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.types.parser.ParseState;
import com.kamijoucen.ruler.types.parser.TokenStream;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.RsonNode;

import com.kamijoucen.ruler.types.common.Constant;
import com.kamijoucen.ruler.types.exception.SyntaxException;
import com.kamijoucen.ruler.types.token.Token;
import com.kamijoucen.ruler.types.token.TokenType;
import com.kamijoucen.ruler.logic.util.AssertUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * RSON（对象）字面量解析器
 */
public class RsonParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        if (tokenStream.token().type != TokenType.LEFT_BRACE) {
            return false;
        }
        Token next = tokenStream.peek(1);
        if (next.type == TokenType.RIGHT_BRACE) {
            return true;
        }
        if (next.type != TokenType.IDENTIFIER && next.type != TokenType.STRING) {
            return false;
        }
        return tokenStream.peek(2).type == TokenType.COLON;
    }

    @Override
    public BaseNode parse(ParseState state) {
        TokenStream tokenStream = state.tokens;

        AssertUtil.assertToken(tokenStream, TokenType.LEFT_BRACE);
        Token lToken = tokenStream.token();
        tokenStream.nextToken();

        Map<String, BaseNode> properties = new HashMap<>();
        if (tokenStream.token().type != TokenType.RIGHT_BRACE) {
            if (tokenStream.token().type != TokenType.IDENTIFIER
                    && tokenStream.token().type != TokenType.STRING) {
                throw new SyntaxException("invalid key\t token=" + tokenStream.token());
            }
            Token name = tokenStream.token();
            if (Constant.isReservedName(name.name)) {
                throw new SyntaxException("reserved name cannot be used as identifier: " + name.name);
            }
            tokenStream.nextToken();

            AssertUtil.assertToken(tokenStream, TokenType.COLON);
            tokenStream.nextToken();
            properties.put(name.name, Parser.parseExpression(state));
        }

        while (tokenStream.token().type != TokenType.RIGHT_BRACE) {
            AssertUtil.assertToken(tokenStream, TokenType.COMMA);
            tokenStream.nextToken();

            if (tokenStream.token().type == TokenType.RIGHT_BRACE) {
                break;
            }
            if (tokenStream.token().type != TokenType.IDENTIFIER
                    && tokenStream.token().type != TokenType.STRING) {
                throw new SyntaxException("invalid key\t token=" + tokenStream.token());
            }
            Token name = tokenStream.token();
            if (Constant.isReservedName(name.name)) {
                throw new SyntaxException("reserved name cannot be used as identifier: " + name.name);
            }
            tokenStream.nextToken();
            AssertUtil.assertToken(tokenStream, TokenType.COLON);
            tokenStream.nextToken();
            properties.put(name.name, Parser.parseExpression(state));
        }

        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_BRACE);
        tokenStream.nextToken();

        return new RsonNode(properties, lToken.location);
    }
}
