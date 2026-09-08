package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.types.parser.ParseState;
import com.kamijoucen.ruler.types.parser.TokenStream;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.BlockNode;
import com.kamijoucen.ruler.types.ast.WhileStatementNode;

import com.kamijoucen.ruler.types.exception.SyntaxException;
import com.kamijoucen.ruler.types.token.Token;
import com.kamijoucen.ruler.types.token.TokenType;
import com.kamijoucen.ruler.logic.util.AssertUtil;

import java.util.Collections;

/**
 * while语句解析器
 */
public class WhileParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.KEY_WHILE;
    }

    @Override
    public BaseNode parse(ParseState state) {
        boolean prevInLoop = state.inLoop;
        state.inLoop = true;
        try {
            TokenStream tokenStream = state.tokens;
            Token whileToken = tokenStream.token();
            AssertUtil.assertToken(whileToken, TokenType.KEY_WHILE);
            tokenStream.nextToken();

            // 解析条件表达式
            BaseNode condition = Parser.parseExpression(state);
            BaseNode blockAST;

            // 解析循环体
            if (tokenStream.token().type == TokenType.LEFT_BRACE) {
                blockAST = Parsers.BLOCK_PARSER.parse(state);
            } else if (tokenStream.token().type == TokenType.COLON) {
                tokenStream.nextToken();
                BaseNode statement = Parser.parseStatement(state);
                blockAST = new BlockNode(Collections.singletonList(statement), statement.getLocation());
            } else {
                throw new SyntaxException("expected '{' or ': after while condition\t token=" + tokenStream.token());
            }

            return new WhileStatementNode(condition, blockAST, whileToken.location);
        } finally {
            // 无论是否解析异常，都需要恢复进入前的循环状态，避免污染后续解析
            state.inLoop = prevInLoop;
        }
    }

}
