package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.component.AtomParser;
import com.kamijoucen.ruler.component.AtomParserManager;
import com.kamijoucen.ruler.component.Parsers;
import com.kamijoucen.ruler.component.TokenStream;
import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.expression.BlockNode;
import com.kamijoucen.ruler.domain.ast.expression.MatchCase;
import com.kamijoucen.ruler.domain.ast.expression.MatchNode;
import com.kamijoucen.ruler.domain.ast.factor.*;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.token.TokenType;
import com.kamijoucen.ruler.logic.util.AssertUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * match表达式解析器
 */
public class MatchParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.KEY_MATCH;
    }

    @Override
    public BaseNode parse(AtomParserManager manager) {
        TokenStream tokenStream = manager.getTokenStream();
        Token matchToken = tokenStream.token();

        AssertUtil.assertToken(matchToken, TokenType.KEY_MATCH);
        tokenStream.nextToken();

        BaseNode scrutinee = manager.parseExpression();

        AssertUtil.assertToken(tokenStream, TokenType.LEFT_BRACE);
        tokenStream.nextToken();

        List<MatchCase> cases = new ArrayList<>();
        while (tokenStream.token().type != TokenType.RIGHT_BRACE
                && tokenStream.token().type != TokenType.EOF) {
            MatchCase matchCase = parseCase(manager);
            cases.add(matchCase);
            // 支持同一行内多 case 用分号分隔
            if (tokenStream.token().type == TokenType.SEMICOLON) {
                tokenStream.nextToken();
            }
        }

        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_BRACE);
        tokenStream.nextToken();

        if (cases.isEmpty()) {
            throw new SyntaxException("match expression must have at least one case", matchToken.location);
        }

        return new MatchNode(scrutinee, cases, matchToken.location);
    }

    private MatchCase parseCase(AtomParserManager manager) {
        TokenStream tokenStream = manager.getTokenStream();
        PatternNode pattern = parsePattern(manager);

        AssertUtil.assertToken(tokenStream, TokenType.ARROW);
        tokenStream.nextToken();

        BaseNode body;
        if (tokenStream.token().type == TokenType.LEFT_BRACE) {
            body = Parsers.BLOCK_PARSER.parse(manager);
        } else {
            BaseNode expr = manager.parseExpression();
            body = new BlockNode(Collections.singletonList(expr), expr.getLocation());
        }

        return new MatchCase(pattern, body);
    }

    private PatternNode parsePattern(AtomParserManager manager) {
        TokenStream tokenStream = manager.getTokenStream();
        Token token = tokenStream.token();

        switch (token.type) {
            case INTEGER:
                tokenStream.nextToken();
                return new LiteralPatternNode(new IntegerNode(new BigInteger(token.name), token.location));
            case DOUBLE:
                tokenStream.nextToken();
                return new LiteralPatternNode(new DoubleNode(new BigDecimal(token.name), token.location));
            case STRING:
                tokenStream.nextToken();
                return new LiteralPatternNode(new StringNode(token.name, token.location));
            case KEY_TRUE:
                tokenStream.nextToken();
                return new LiteralPatternNode(new BoolNode(true, token.location));
            case KEY_FALSE:
                tokenStream.nextToken();
                return new LiteralPatternNode(new BoolNode(false, token.location));
            case KEY_NULL:
                tokenStream.nextToken();
                return new LiteralPatternNode(new NullNode(token.location));
            case IDENTIFIER:
                tokenStream.nextToken();
                if ("_".equals(token.name)) {
                    return WildcardPatternNode.INSTANCE;
                }
                return new NamePatternNode(token.name);
            default:
                throw new SyntaxException("unsupported pattern type: " + token.type, token.location);
        }
    }

}
