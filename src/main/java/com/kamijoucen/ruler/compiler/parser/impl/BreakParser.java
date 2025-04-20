package com.kamijoucen.ruler.compiler.parser.impl;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.factor.BreakNode;
import com.kamijoucen.ruler.common.MessageType;
import com.kamijoucen.ruler.compiler.TokenStream;
import com.kamijoucen.ruler.compiler.parser.AtomParser;
import com.kamijoucen.ruler.compiler.parser.AtomParserManager;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.util.AssertUtil;

/**
 * break语句解析器
 */
public class BreakParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.KEY_BREAK;
    }

    @Override
    public BaseNode parse(AtomParserManager manager) {
        TokenStream tokenStream = manager.getTokenStream();
        Token token = tokenStream.token();

        AssertUtil.assertToken(token, TokenType.KEY_BREAK);

        // 检查是否在循环内
        if (!manager.isInLoop()) {
            String message = manager.getConfiguration().getMessageManager()
                    .buildMessage(MessageType.BREAK_NOT_IN_LOOP, token.location);
            throw new SyntaxException(message);
        }

        tokenStream.nextToken();
        return new BreakNode(token.location);
    }
}