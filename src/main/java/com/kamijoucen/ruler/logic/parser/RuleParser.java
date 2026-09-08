package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.types.parser.ParseState;
import com.kamijoucen.ruler.types.parser.TokenStream;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.BlockNode;
import com.kamijoucen.ruler.types.ast.RuleStatementNode;
import com.kamijoucen.ruler.types.ast.StringNode;

import com.kamijoucen.ruler.types.exception.SyntaxException;
import com.kamijoucen.ruler.types.token.Token;
import com.kamijoucen.ruler.types.token.TokenType;
import com.kamijoucen.ruler.logic.util.AssertUtil;

/**
 * rule语句解析器
 */
public class RuleParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.KEY_RULE;
    }

    @Override
    public BaseNode parse(ParseState state) {
        TokenStream tokenStream = state.tokens;
        Token ruleToken = tokenStream.token();

        AssertUtil.assertToken(ruleToken, TokenType.KEY_RULE);
        tokenStream.nextToken();

        // 解析规则名称
        AssertUtil.assertToken(tokenStream, TokenType.STRING);
        Token nameToken = tokenStream.token();
        tokenStream.nextToken();

        // 解析规则代码块
        BaseNode blockNode = Parsers.BLOCK_PARSER.parse(state);
        if (!(blockNode instanceof BlockNode)) {
            throw new SyntaxException("expected block after rule");
        }

        return new RuleStatementNode(
                new StringNode(nameToken.name, nameToken.location),
                (BlockNode) blockNode,
                ruleToken.location);
    }
}
