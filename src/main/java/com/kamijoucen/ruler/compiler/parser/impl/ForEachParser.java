package com.kamijoucen.ruler.compiler.parser.impl;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.BlockNode;
import com.kamijoucen.ruler.ast.expression.ForEachStatementNode;
import com.kamijoucen.ruler.compiler.TokenStream;
import com.kamijoucen.ruler.compiler.parser.AtomParser;
import com.kamijoucen.ruler.compiler.parser.AtomParserManager;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.util.AssertUtil;

import java.util.Collections;

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
        boolean inLoop = manager.isInLoop();
        manager.setInLoop(true);

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
            blockNode = BlockParser.parseBlock(manager);
        } else if (tokenStream.token().type == TokenType.COLON) {
            tokenStream.nextToken();
            BaseNode statement = manager.parseStatement();
            blockNode = new BlockNode(Collections.singletonList(statement), statement.getLocation());
        } else {
            throw SyntaxException.withSyntax("for循环条件后应该跟随'{'或':'", tokenStream.token());
        }

        // 恢复循环状态
        if (!inLoop) {
            manager.setInLoop(false);
        }

        return new ForEachStatementNode(name, arrayExp, blockNode, forToken.location);
    }
}