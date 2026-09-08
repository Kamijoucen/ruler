package com.kamijoucen.ruler.logic.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.AssignNode;
import com.kamijoucen.ruler.types.ast.CallNode;
import com.kamijoucen.ruler.types.ast.DotNode;
import com.kamijoucen.ruler.types.ast.ImportNode;
import com.kamijoucen.ruler.types.ast.IndexNode;
import com.kamijoucen.ruler.types.ast.BinaryOperationNode;
import com.kamijoucen.ruler.types.ast.NameNode;
import com.kamijoucen.ruler.types.common.OperationDefine;
import com.kamijoucen.ruler.types.parser.ParseState;
import com.kamijoucen.ruler.types.parser.TokenStream;
import com.kamijoucen.ruler.logic.lexer.Lexer;

import java.util.Arrays;
import java.util.Collections;

import com.kamijoucen.ruler.types.exception.SyntaxException;
import com.kamijoucen.ruler.types.token.Token;
import com.kamijoucen.ruler.types.token.TokenLocation;
import com.kamijoucen.ruler.types.token.TokenType;
import com.kamijoucen.ruler.logic.util.AssertUtil;

public final class Parser {
    private Parser() {
    }

    private static final List<AtomParser> STATEMENT_PARSERS = Collections.unmodifiableList(Arrays.asList(
        Parsers.VAR_PARSER, Parsers.RETURN_PARSER, Parsers.BREAK_PARSER, Parsers.CONTINUE_PARSER,
        Parsers.RULE_PARSER, Parsers.INFIX_PARSER, Parsers.IF_PARSER, Parsers.WHILE_PARSER,
        Parsers.FOR_EACH_PARSER, Parsers.FUN_PARSER, Parsers.MATCH_PARSER));
    // RSON must precede block parsing because both begin with a left brace.
    private static final List<AtomParser> EXPRESSION_PARSERS = Collections.unmodifiableList(Arrays.asList(
        Parsers.IDENTIFIER_PARSER, Parsers.BOOL_PARSER, Parsers.PAREN_PARSER,
        Parsers.UNARY_EXPRESSION_PARSER, Parsers.NUMBER_PARSER, Parsers.STRING_PARSER,
        Parsers.NULL_PARSER, Parsers.ARRAY_PARSER, Parsers.RSON_PARSER, Parsers.BLOCK_PARSER,
        Parsers.TYPE_OF_PARSER, Parsers.IF_PARSER, Parsers.WHILE_PARSER,
        Parsers.FOR_EACH_PARSER, Parsers.FUN_PARSER, Parsers.MATCH_PARSER));

    public static ImportNode parseImport(ParseState state) {
        // 导入语句解析逻辑
        if (state.tokens.token().type != TokenType.KEY_IMPORT) {
            throw new IllegalArgumentException(
                "Expected KEY_IMPORT token, but got " + state.tokens.token().type);
        }
        Token importToken = state.tokens.token();
        state.tokens.nextToken();

        boolean hasImportInfix = false;
        if (state.tokens.token().type == TokenType.KEY_INFIX) {
            hasImportInfix = true;
            state.tokens.nextToken();
        }

        AssertUtil.assertToken(state.tokens, TokenType.STRING);
        String path = state.tokens.token().name;
        state.tokens.nextToken();

        Token aliasToken = null;
        if (state.tokens.token().type == TokenType.IDENTIFIER) {
            aliasToken = state.tokens.token();
            state.tokens.nextToken();
        }

        // 不允许出现无别名切无中缀标识的导入语句
        if (aliasToken == null && !hasImportInfix) {
            throw new SyntaxException(
                "import statement without alias and infix is not allowed",
                importToken.location);
        }
        if (state.tokens.token().type == TokenType.SEMICOLON) {
            state.tokens.nextToken();
        } else if (!state.tokens.isNewLine() && state.tokens.token().type != TokenType.EOF) {
            throw new SyntaxException("expected semicolon or newline after import statement", state.tokens.token().location);
        }
        return new ImportNode(path, aliasToken == null ? null : aliasToken.name, hasImportInfix,
            importToken.location);
    }

    public static BaseNode parseStatement(ParseState state) {
        Token token = state.tokens.token();
        boolean wasRoot = state.root;
        if (token.type == TokenType.KEY_RULE || token.type == TokenType.KEY_INFIX) {
            if (!wasRoot) {
                throw new UnsupportedOperationException();
            }
        }
        if (wasRoot) {
            state.root = false;
        }

        try {
            // 查找适合的语句解析器并执行解析
            BaseNode statement = null;
            boolean isNeedSemicolon = true;

            for (AtomParser parser : STATEMENT_PARSERS) {
                if (parser.support(state.tokens)) { // 使用整个tokenStream代替单个token
                    statement = parser.parse(state);
                    // 根据具体解析器类型确定是否需要分号
                    isNeedSemicolon = needSemicolon(parser);
                    break;
                }
            }

            if (statement == null) {
                statement = parseExpression(state);
            }

            if (isNeedSemicolon) {
                if (state.tokens.token().type == TokenType.SEMICOLON) {
                    state.tokens.nextToken();
                } else if (!state.tokens.isNewLine() && state.tokens.token().type != TokenType.EOF
                    && state.tokens.token().type != TokenType.RIGHT_BRACE) {
                    throw new SyntaxException("expected semicolon or newline after statement",
                        state.tokens.token().location);
                }
            }

            if (statement == null) {
                throw new SyntaxException("unknown expression start '" + token.name + "'", token.location);
            }

            return statement;
        } finally {
            if (wasRoot) {
                state.root = true;
            }
        }
    }

    // 判断特定解析器是否需要分号
    private static boolean needSemicolon(AtomParser parser) {
        return !(parser instanceof IfParser || parser instanceof WhileParser
            || parser instanceof ForEachParser || parser instanceof FunParser
            || parser instanceof RuleParser || parser instanceof InfixParser
            || parser instanceof BlockParser || parser instanceof MatchParser);
    }

    public static BaseNode parseExpression(ParseState state) {
        BaseNode lhs = parsePrimaryExpression(state);
        if (lhs == null) {
            Token token = state.tokens.token();
            throw new SyntaxException("unknown expression start '" + token.name + "'", token.location);
        }
        return parseBinaryNode(state, 0, lhs);
    }

    public static BaseNode parseExpression(String expression, TokenLocation location) {
        ParseState state = new ParseState(new TokenStream(Lexer.scan(expression, location.fileName)));
        TokenStream stream = state.tokens;
        try {
            BaseNode node = parseExpression(state);
            if (stream.token().type != TokenType.EOF) {
                throw new SyntaxException(
                    "illegal string interpolation expression '" + expression + "'",
                    location);
            }
            return node;
        } catch (NullPointerException e) {
            throw new SyntaxException(
                "illegal string interpolation expression '" + expression + "'",
                location);
        }
    }

    private static BaseNode parseBinaryNode(ParseState state, int expPrec, BaseNode lhs) {
        while (true) {
            Token curOpToken = state.tokens.token();
            if (curOpToken.type == TokenType.PIPE) {
                throw new SyntaxException(
                    "\"|\" is only allowed in pattern matching",
                    curOpToken.location);
            }
            int curTokenProc = OperationDefine.findPrecedence(curOpToken.type);
            if (curTokenProc < expPrec) {
                return lhs;
            }
            if (curOpToken.type == TokenType.IDENTIFIER && state.tokens.isNewLine()) {
                return lhs;
            }

            state.tokens.nextToken();
            BaseNode rhs = parsePrimaryExpression(state);
            Objects.requireNonNull(rhs);

            Token nextToken = state.tokens.token();
            int nextTokenProc = OperationDefine.findPrecedence(nextToken.type);
            if (curTokenProc < nextTokenProc) {
                rhs = parseBinaryNode(state, curTokenProc + 1, rhs);
                Objects.requireNonNull(rhs);
            }

            if (curOpToken.type == TokenType.ASSIGN) {
                if (!(lhs instanceof IndexNode) && !(lhs instanceof NameNode)
                    && !(lhs instanceof DotNode)) {
                    throw new SyntaxException("invalid assignment target\t token=" + curOpToken);
                }
                lhs = new AssignNode(lhs, rhs, lhs.getLocation());
            } else {
                lhs = new BinaryOperationNode(curOpToken.type, curOpToken.name, lhs, rhs,
                    lhs.getLocation());
            }
        }
    }

    public static BaseNode parsePrimaryExpression(ParseState state) {
        BaseNode node = null;
        // 使用表达式解析器解析基本表达式
        for (AtomParser parser : EXPRESSION_PARSERS) {
            if (parser.support(state.tokens)) { // 使用整个tokenStream代替单个token
                node = parser.parse(state);
                break;
            }
        }

        // 如果没有找到合适的解析器，返回null
        if (node == null) {
            return null;
        }

        // 处理链式调用，如 a.b(), a[b], a.b 等
        while (state.tokens.token().type == TokenType.DOT
            || state.tokens.token().type == TokenType.LEFT_PAREN
            || state.tokens.token().type == TokenType.LEFT_SQUARE) {
            if (state.tokens.token().type == TokenType.LEFT_PAREN) {
                // 函数调用
                state.tokens.nextToken();
                List<BaseNode> params = new ArrayList<>();
                if (state.tokens.token().type != TokenType.RIGHT_PAREN) {
                    params.add(parseExpression(state));
                }
                while (state.tokens.token().type != TokenType.RIGHT_PAREN) {
                    AssertUtil.assertToken(state.tokens, TokenType.COMMA);
                    state.tokens.nextToken();
                    params.add(parseExpression(state));
                }
                AssertUtil.assertToken(state.tokens, TokenType.RIGHT_PAREN);
                state.tokens.nextToken();
                node = new CallNode(node, params, node.getLocation());
            } else if (state.tokens.token().type == TokenType.LEFT_SQUARE) {
                // 数组索引
                state.tokens.nextToken();

                BaseNode indexNode = parseExpression(state);
                Objects.requireNonNull(indexNode);

                AssertUtil.assertToken(state.tokens, TokenType.RIGHT_SQUARE);
                state.tokens.nextToken();
                node = new IndexNode(node, indexNode, node.getLocation());
            } else {
                // 对象属性访问
                state.tokens.nextToken();
                Token dotNameNode = state.tokens.token();
                AssertUtil.assertToken(dotNameNode, TokenType.IDENTIFIER);
                // only identifiers are supported for dot call
                BaseNode nameNode = Parsers.IDENTIFIER_PARSER.parse(state);
                node = new DotNode(node, nameNode, node.getLocation());
            }
        }
        return node;
    }

}
