package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.types.parser.ParseState;
import com.kamijoucen.ruler.types.parser.TokenStream;

import java.util.ArrayList;
import java.util.List;
import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.ReturnNode;

import com.kamijoucen.ruler.types.token.Token;
import com.kamijoucen.ruler.types.token.TokenType;
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
    public BaseNode parse(ParseState state) {
        TokenStream tokenStream = state.tokens;

        AssertUtil.assertToken(tokenStream, TokenType.KEY_RETURN);
        Token returnToken = tokenStream.token();
        tokenStream.nextToken();

        List<BaseNode> param = new ArrayList<>();
        if (!isReturnEnd(tokenStream)) {
            param.add(Parser.parseExpression(state));
        }

        while (!isReturnEnd(tokenStream)) {
            AssertUtil.assertToken(tokenStream, TokenType.COMMA);
            tokenStream.nextToken();
            param.add(Parser.parseExpression(state));
        }

        return new ReturnNode(param, returnToken.location);
    }

    private boolean isReturnEnd(TokenStream tokenStream) {
        TokenType t = tokenStream.token().type;
        return t == TokenType.SEMICOLON || t == TokenType.RIGHT_BRACE || t == TokenType.EOF
                || tokenStream.isNewLine();
    }
}
