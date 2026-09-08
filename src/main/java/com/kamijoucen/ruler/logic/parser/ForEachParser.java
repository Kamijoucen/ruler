package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.types.parser.ParseState;
import com.kamijoucen.ruler.types.parser.TokenStream;

import java.util.Collections;
import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.BlockNode;
import com.kamijoucen.ruler.types.ast.ForEachStatementNode;

import com.kamijoucen.ruler.types.exception.SyntaxException;
import com.kamijoucen.ruler.types.token.Token;
import com.kamijoucen.ruler.types.token.TokenType;
import com.kamijoucen.ruler.logic.util.AssertUtil;

/**
 * foreach语句解析器
 */
public class ForEachParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.KEY_FOR;
    }

    @Override
    public BaseNode parse(ParseState state) {
        boolean prevInLoop = state.inLoop;
        state.inLoop = true;
        try {
            TokenStream tokenStream = state.tokens;
            Token forToken = tokenStream.token();
            AssertUtil.assertToken(forToken, TokenType.KEY_FOR);
            tokenStream.nextToken();

            // 解析循环变量名
            AssertUtil.assertToken(tokenStream, TokenType.IDENTIFIER);
            Token name = tokenStream.token();
            tokenStream.nextToken();

            // 解析 in 关键字
            AssertUtil.assertToken(tokenStream, TokenType.KEY_IN);
            tokenStream.nextToken();

            // 解析集合表达式
            BaseNode arrayExp = Parser.parseExpression(state);

            // 解析循环体
            BaseNode blockNode;
            if (tokenStream.token().type == TokenType.LEFT_BRACE) {
                blockNode = Parsers.BLOCK_PARSER.parse(state);
            } else if (tokenStream.token().type == TokenType.COLON) {
                tokenStream.nextToken();
                BaseNode statement = Parser.parseStatement(state);
                blockNode = new BlockNode(Collections.singletonList(statement), statement.getLocation());
            } else {
                throw new SyntaxException("expected '{' or ':' after for condition\t token=" + tokenStream.token());
            }

            return new ForEachStatementNode(name, arrayExp, blockNode, forToken.location);
        } finally {
            // 无论是否解析异常，都需要恢复进入前的循环状态，避免污染后续解析
            state.inLoop = prevInLoop;
        }
    }
}
