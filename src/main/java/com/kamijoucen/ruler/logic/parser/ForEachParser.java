package com.kamijoucen.ruler.logic.parser;

import java.util.Collections;
import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.expression.BlockNode;
import com.kamijoucen.ruler.domain.ast.expression.ForEachStatementNode;
import com.kamijoucen.ruler.component.Parsers;
import com.kamijoucen.ruler.component.TokenStream;
import com.kamijoucen.ruler.component.AtomParser;
import com.kamijoucen.ruler.component.AtomParserManager;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.token.TokenType;
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
    public BaseNode parse(AtomParserManager manager) {
        boolean prevInLoop = manager.isInLoop();
        manager.setInLoop(true);
        try {
            TokenStream tokenStream = manager.getTokenStream();
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
            BaseNode arrayExp = manager.parseExpression();

            // 解析循环体
            BaseNode blockNode;
            if (tokenStream.token().type == TokenType.LEFT_BRACE) {
                blockNode = Parsers.BLOCK_PARSER.parse(manager);
            } else if (tokenStream.token().type == TokenType.COLON) {
                tokenStream.nextToken();
                BaseNode statement = manager.parseStatement();
                blockNode = new BlockNode(Collections.singletonList(statement), statement.getLocation());
            } else {
                throw new SyntaxException("expected '{' or ':' after for condition\t token=" + tokenStream.token());
            }

            return new ForEachStatementNode(name, arrayExp, blockNode, forToken.location);
        } finally {
            // 无论是否解析异常，都需要恢复进入前的循环状态，避免污染后续解析
            manager.setInLoop(prevInLoop);
        }
    }
}