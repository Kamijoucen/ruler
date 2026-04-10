package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.factor.BreakNode;
import com.kamijoucen.ruler.domain.common.MessageType;
import com.kamijoucen.ruler.component.TokenStream;
import com.kamijoucen.ruler.component.AtomParser;
import com.kamijoucen.ruler.component.AtomParserManager;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.token.TokenType;
import com.kamijoucen.ruler.logic.util.AssertUtil;

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