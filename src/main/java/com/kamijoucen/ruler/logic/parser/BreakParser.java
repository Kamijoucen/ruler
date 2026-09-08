package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.types.parser.ParseState;
import com.kamijoucen.ruler.types.parser.TokenStream;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.BreakNode;

import com.kamijoucen.ruler.types.exception.SyntaxException;
import com.kamijoucen.ruler.types.token.Token;
import com.kamijoucen.ruler.types.token.TokenType;
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
    public BaseNode parse(ParseState state) {
        TokenStream tokenStream = state.tokens;
        Token token = tokenStream.token();

        AssertUtil.assertToken(token, TokenType.KEY_BREAK);

        if (!state.inLoop) {
            throw new SyntaxException("break must be inside a loop", token.location);
        }

        tokenStream.nextToken();
        return new BreakNode(token.location);
    }
}
