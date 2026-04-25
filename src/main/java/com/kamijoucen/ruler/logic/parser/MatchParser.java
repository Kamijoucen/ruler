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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        BaseNode guard = null;
        if (tokenStream.token().type == TokenType.KEY_IF) {
            tokenStream.nextToken();
            guard = manager.parseExpression();
        }

        AssertUtil.assertToken(tokenStream, TokenType.ARROW);
        tokenStream.nextToken();

        BaseNode body;
        if (tokenStream.token().type == TokenType.LEFT_BRACE) {
            body = Parsers.BLOCK_PARSER.parse(manager);
        } else {
            BaseNode stmt = manager.parseStatement();
            body = new BlockNode(Collections.singletonList(stmt), stmt.getLocation());
        }

        return new MatchCase(pattern, guard, body);
    }

    private PatternNode parsePattern(AtomParserManager manager) {
        PatternNode left = parsePrimaryPattern(manager);
        TokenStream tokenStream = manager.getTokenStream();
        if (tokenStream.token().type != TokenType.PIPE) {
            return left;
        }
        List<PatternNode> alternatives = new ArrayList<>();
        alternatives.add(left);
        while (tokenStream.token().type == TokenType.PIPE) {
            tokenStream.nextToken();
            alternatives.add(parsePrimaryPattern(manager));
        }
        return new OrPatternNode(alternatives);
    }

    private PatternNode parsePrimaryPattern(AtomParserManager manager) {
        TokenStream tokenStream = manager.getTokenStream();
        Token token = tokenStream.token();

        // 负数字面量模式：仅当 `-` 紧跟 INTEGER 或 DOUBLE 时解析为负数字面量模式。
        // 其它组合保持原有错误路径（default 抛 unsupported pattern type: SUB）。
        if (token.type == TokenType.SUB) {
            Token next = tokenStream.peek();
            if (next.type == TokenType.INTEGER) {
                tokenStream.nextToken();   // 消费 -
                tokenStream.nextToken();   // 消费 INTEGER
                return new LiteralPatternNode(
                        new IntegerNode(new BigInteger(next.name).negate(), token.location));
            }
            if (next.type == TokenType.DOUBLE) {
                tokenStream.nextToken();
                tokenStream.nextToken();
                return new LiteralPatternNode(
                        new DoubleNode(new BigDecimal(next.name).negate(), token.location));
            }
        }

        switch (token.type) {
            case LEFT_SQUARE:
                return parseArrayPattern(manager);
            case LEFT_BRACE:
                return parseObjectPattern(manager);
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
            case KEY_TYPEOF:
                tokenStream.nextToken();
                Token typeToken = tokenStream.token();
                AssertUtil.assertToken(typeToken, TokenType.STRING);
                tokenStream.nextToken();
                return new TypeofPatternNode(typeToken.name);
            case KEY_VAR:
                tokenStream.nextToken();
                Token bindToken = tokenStream.token();
                AssertUtil.assertToken(bindToken, TokenType.IDENTIFIER);
                tokenStream.nextToken();
                return new NamePatternNode(bindToken.name);
            case IDENTIFIER:
                tokenStream.nextToken();
                if ("_".equals(token.name)) {
                    return WildcardPatternNode.INSTANCE;
                }
                // bare 标识符：值比较（从 scope 中查找变量值）
                return new LiteralPatternNode(new NameNode(token, token.location));
            default:
                throw new SyntaxException("unsupported pattern type: " + token.type, token.location);
        }
    }

    private PatternNode parseArrayPattern(AtomParserManager manager) {
        TokenStream tokenStream = manager.getTokenStream();
        Token startToken = tokenStream.token();
        AssertUtil.assertToken(startToken, TokenType.LEFT_SQUARE);
        tokenStream.nextToken();

        List<PatternNode> elements = new ArrayList<>();
        RestPatternNode restPattern = null;

        while (tokenStream.token().type != TokenType.RIGHT_SQUARE
                && tokenStream.token().type != TokenType.EOF) {

            if (tokenStream.token().type == TokenType.DOT_DOT_DOT) {
                tokenStream.nextToken();
                Token restToken = tokenStream.token();
                // 支持 ...var tail 和 ...tail 两种语法
                if (restToken.type == TokenType.KEY_VAR) {
                    tokenStream.nextToken();
                    restToken = tokenStream.token();
                    AssertUtil.assertToken(restToken, TokenType.IDENTIFIER);
                    tokenStream.nextToken();
                } else if (restToken.type == TokenType.IDENTIFIER) {
                    tokenStream.nextToken();
                } else {
                    throw new SyntaxException("expected identifier or 'var' after '...' in array pattern", restToken.location);
                }
                if ("_".equals(restToken.name)) {
                    restPattern = new RestPatternNode(null);
                } else {
                    restPattern = new RestPatternNode(restToken.name);
                }
                break;
            }

            elements.add(parsePattern(manager));

            if (tokenStream.token().type == TokenType.COMMA) {
                tokenStream.nextToken();
            } else if (tokenStream.token().type != TokenType.RIGHT_SQUARE) {
                throw new SyntaxException("expected ',' or ']' in array pattern", tokenStream.token().location);
            }
        }

        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_SQUARE);
        tokenStream.nextToken();

        return new ArrayPatternNode(elements, restPattern);
    }

    private PatternNode parseObjectPattern(AtomParserManager manager) {
        TokenStream tokenStream = manager.getTokenStream();
        Token startToken = tokenStream.token();
        AssertUtil.assertToken(startToken, TokenType.LEFT_BRACE);
        tokenStream.nextToken();

        List<ObjectPatternField> fields = new ArrayList<>();
        Set<String> seenFieldNames = new HashSet<>();
        RestPatternNode restPattern = null;

        while (tokenStream.token().type != TokenType.RIGHT_BRACE
                && tokenStream.token().type != TokenType.EOF) {

            if (tokenStream.token().type == TokenType.DOT_DOT_DOT) {
                tokenStream.nextToken();
                Token restToken = tokenStream.token();
                // 支持 ...var rest 和 ...rest 两种语法
                if (restToken.type == TokenType.KEY_VAR) {
                    tokenStream.nextToken();
                    restToken = tokenStream.token();
                    AssertUtil.assertToken(restToken, TokenType.IDENTIFIER);
                    tokenStream.nextToken();
                } else if (restToken.type == TokenType.IDENTIFIER) {
                    tokenStream.nextToken();
                } else {
                    throw new SyntaxException("expected identifier or 'var' after '...' in object pattern", restToken.location);
                }
                if ("_".equals(restToken.name)) {
                    restPattern = new RestPatternNode(null);
                } else {
                    restPattern = new RestPatternNode(restToken.name);
                }
                break;
            }

            // 字段名
            Token fieldNameToken = tokenStream.token();
            if (fieldNameToken.type != TokenType.IDENTIFIER && fieldNameToken.type != TokenType.STRING) {
                throw new SyntaxException("expected identifier or string as object pattern field name", fieldNameToken.location);
            }
            tokenStream.nextToken();
            String fieldName = fieldNameToken.name;

            if (!seenFieldNames.add(fieldName)) {
                throw new SyntaxException(
                        "duplicate field in object pattern: " + fieldName, fieldNameToken.location);
            }

            AssertUtil.assertToken(tokenStream, TokenType.COLON);
            tokenStream.nextToken();

            PatternNode fieldPattern = parsePattern(manager);
            fields.add(new ObjectPatternField(fieldName, fieldPattern));

            if (tokenStream.token().type == TokenType.COMMA) {
                tokenStream.nextToken();
            } else if (tokenStream.token().type != TokenType.RIGHT_BRACE) {
                throw new SyntaxException("expected ',' or '}' in object pattern", tokenStream.token().location);
            }
        }

        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_BRACE);
        tokenStream.nextToken();

        return new ObjectPatternNode(fields, restPattern);
    }

}
