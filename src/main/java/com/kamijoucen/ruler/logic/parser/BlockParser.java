package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.types.parser.ParseState;
import com.kamijoucen.ruler.types.parser.TokenStream;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.BlockNode;

import com.kamijoucen.ruler.types.token.Token;
import com.kamijoucen.ruler.types.token.TokenType;
import com.kamijoucen.ruler.logic.util.AssertUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 代码块解析器
 */
public class BlockParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.LEFT_BRACE;
    }

    @Override
    public BaseNode parse(ParseState state) {
        TokenStream tokenStream = state.tokens;
        Token lToken = tokenStream.token();
        AssertUtil.assertToken(lToken, TokenType.LEFT_BRACE);
        tokenStream.nextToken();

        List<BaseNode> blocks = new ArrayList<>();
        while (tokenStream.token().type != TokenType.EOF
                && tokenStream.token().type != TokenType.RIGHT_BRACE) {
            blocks.add(Parser.parseStatement(state));
        }

        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_BRACE);
        tokenStream.nextToken();
        return new BlockNode(blocks, lToken.location);
    }
}
