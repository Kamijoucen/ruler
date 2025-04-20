package com.kamijoucen.ruler.compiler.parser.impl;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.BlockNode;
import com.kamijoucen.ruler.ast.expression.IfStatementNode;
import com.kamijoucen.ruler.compiler.Parsers;
import com.kamijoucen.ruler.compiler.TokenStream;
import com.kamijoucen.ruler.compiler.parser.AtomParser;
import com.kamijoucen.ruler.compiler.parser.AtomParserManager;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.util.AssertUtil;

import java.util.Collections;

/**
 * if语句解析器
 */
public class IfParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        // 使用TokenStream而不是单个token，可以实现更复杂的判断逻辑
        // 比如检查 if 后面是否跟着有效的条件表达式
        return tokenStream.token().type == TokenType.KEY_IF;
    }

    @Override
    public BaseNode parse(AtomParserManager manager) {
        TokenStream tokenStream = manager.getTokenStream();
        Token ifToken = tokenStream.token();

        AssertUtil.assertToken(ifToken, TokenType.KEY_IF);
        tokenStream.nextToken();

        BaseNode condition = manager.parseExpression();
        BaseNode thenBlock;

        if (tokenStream.token().type == TokenType.LEFT_BRACE) {
            thenBlock = Parsers.BLOCK_PARSER.parse(manager);
        } else if (tokenStream.token().type == TokenType.COLON) {
            tokenStream.nextToken();
            BaseNode statement = manager.parseStatement();
            thenBlock = new BlockNode(Collections.singletonList(statement), statement.getLocation());
        } else {
            throw SyntaxException.withSyntax("if条件表达式后应该跟随'{'或':'", tokenStream.token());
        }

        BaseNode elseBlock = null;
        if (tokenStream.token().type == TokenType.KEY_ELSE) {
            Token token = tokenStream.nextToken();
            if (token.type == TokenType.LEFT_BRACE) {
                elseBlock = Parsers.BLOCK_PARSER.parse(manager);
            } else if (token.type == TokenType.KEY_IF) {
                // 支持else if的情况
                elseBlock = parse(manager);
            } else {
                BaseNode statement = manager.parseStatement();
                elseBlock = new BlockNode(Collections.singletonList(statement), statement.getLocation());
            }
        }
        return new IfStatementNode(condition, thenBlock, elseBlock, ifToken.location);
    }
}