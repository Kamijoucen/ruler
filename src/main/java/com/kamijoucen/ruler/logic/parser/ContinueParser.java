package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.types.parser.ParseState;
import com.kamijoucen.ruler.types.parser.TokenStream;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.ContinueNode;

import com.kamijoucen.ruler.types.exception.SyntaxException;
import com.kamijoucen.ruler.types.token.Token;
import com.kamijoucen.ruler.types.token.TokenType;
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
    public BaseNode parse(ParseState state) {
        TokenStream tokenStream = state.tokens;

        if (!state.inLoop) {
            throw new SyntaxException("continue must be inside a loop\t token=" + tokenStream.token());
        }

        AssertUtil.assertToken(tokenStream, TokenType.KEY_CONTINUE);
        Token token = tokenStream.token();
        tokenStream.nextToken();

        return new ContinueNode(token.location);
    }
}
