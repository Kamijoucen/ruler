package com.kamijoucen.ruler.compiler.parser.impl;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.BlockNode;
import com.kamijoucen.ruler.ast.expression.WhileStatementNode;
import com.kamijoucen.ruler.compiler.TokenStream;
import com.kamijoucen.ruler.compiler.parser.AtomParser;
import com.kamijoucen.ruler.compiler.parser.AtomParserManager;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.util.AssertUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * while语句解析器
 */
public class WhileParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.KEY_WHILE;
    }

    @Override
    public BaseNode parse(AtomParserManager manager) {
        boolean inLoop = manager.isInLoop();
        manager.setInLoop(true);

        TokenStream tokenStream = manager.getTokenStream();
        Token whileToken = tokenStream.token();
        AssertUtil.assertToken(whileToken, TokenType.KEY_WHILE);
        tokenStream.nextToken();

        // 解析条件表达式
        BaseNode condition = manager.parseExpression();
        BaseNode blockAST;

        // 解析循环体
        if (tokenStream.token().type == TokenType.LEFT_BRACE) {
            blockAST = parseBlock(manager);
        } else if (tokenStream.token().type == TokenType.COLON) {
            tokenStream.nextToken();
            BaseNode statement = manager.parseStatement();
            blockAST = new BlockNode(Collections.singletonList(statement), statement.getLocation());
        } else {
            throw SyntaxException.withSyntax("while condition expression expected ':' or '{'",
                    tokenStream.token());
        }

        // 恢复循环状态
        if (!inLoop) {
            manager.setInLoop(false);
        }

        return new WhileStatementNode(condition, blockAST, whileToken.location);
    }

    // 解析代码块
    private BaseNode parseBlock(AtomParserManager manager) {
        TokenStream tokenStream = manager.getTokenStream();
        Token lToken = tokenStream.token();
        AssertUtil.assertToken(lToken, TokenType.LEFT_BRACE);
        tokenStream.nextToken();

        List<BaseNode> blocks = new ArrayList<>();
        while (tokenStream.token().type != TokenType.EOF
                && tokenStream.token().type != TokenType.RIGHT_BRACE) {
            blocks.add(manager.parseStatement());
        }

        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_BRACE);
        tokenStream.nextToken();
        return new BlockNode(blocks, lToken.location);
    }
}