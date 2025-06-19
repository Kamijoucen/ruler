package com.kamijoucen.ruler.compiler.parser.impl;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.BlockNode;
import com.kamijoucen.ruler.ast.expression.RuleStatementNode;
import com.kamijoucen.ruler.ast.factor.StringNode;
import com.kamijoucen.ruler.compiler.Parsers;
import com.kamijoucen.ruler.compiler.TokenStream;
import com.kamijoucen.ruler.compiler.parser.AtomParser;
import com.kamijoucen.ruler.compiler.parser.AtomParserManager;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.util.AssertUtil;

/**
 * rule语句解析器
 * 解析rule关键字定义的规则块
 *
 * @author Kamijoucen
 */
public class RuleParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.KEY_RULE;
    }

    @Override
    public BaseNode parse(AtomParserManager manager) {
        TokenStream tokenStream = manager.getTokenStream();
        Token ruleToken = tokenStream.token();

        AssertUtil.assertToken(ruleToken, TokenType.KEY_RULE);
        tokenStream.nextToken();

        // 解析规则名称
        AssertUtil.assertToken(tokenStream, TokenType.STRING);
        Token nameToken = tokenStream.token();
        tokenStream.nextToken();

        // 解析规则代码块
        BaseNode blockNode = Parsers.BLOCK_PARSER.parse(manager);
        if (!(blockNode instanceof BlockNode)) {
            throw new SyntaxException("解析rule语句时出错：rule后应该跟随代码块", ruleToken.location);
        }

        return new RuleStatementNode(
                new StringNode(nameToken.name, nameToken.location),
                (BlockNode) blockNode,
                ruleToken.location);
    }
}
