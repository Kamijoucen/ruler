package com.kamijoucen.ruler.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.expression.AssignNode;
import com.kamijoucen.ruler.domain.ast.expression.CallNode;
import com.kamijoucen.ruler.domain.ast.expression.DotNode;
import com.kamijoucen.ruler.domain.ast.expression.ImportNode;
import com.kamijoucen.ruler.domain.ast.expression.IndexNode;
import com.kamijoucen.ruler.domain.ast.factor.BinaryOperationNode;
import com.kamijoucen.ruler.domain.ast.factor.NameNode;
import com.kamijoucen.ruler.domain.common.OperationDefine;
import com.kamijoucen.ruler.logic.parser.BlockParser;
import com.kamijoucen.ruler.logic.parser.ForEachParser;
import com.kamijoucen.ruler.logic.parser.FunParser;
import com.kamijoucen.ruler.logic.parser.IdentifierParser;
import com.kamijoucen.ruler.logic.parser.IfParser;
import com.kamijoucen.ruler.logic.parser.InfixParser;
import com.kamijoucen.ruler.logic.parser.MatchParser;
import com.kamijoucen.ruler.logic.parser.RuleParser;
import com.kamijoucen.ruler.logic.parser.WhileParser;
import com.kamijoucen.ruler.application.RulerConfiguration;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.logic.operation.BinaryOperation;
import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.token.TokenType;
import com.kamijoucen.ruler.logic.util.AssertUtil;

public class AtomParserManager implements Parser {

    private final List<AtomParser> statementParsers = new ArrayList<>();
    private final List<AtomParser> expressionParsers = new ArrayList<>();
    private final TokenStream tokenStream;
    private final ParseContext parseContext;
    private final RulerConfiguration configuration;
    private final IdentifierParser identifierParser = Parsers.IDENTIFIER_PARSER;

    public AtomParserManager(TokenStream tokenStream, RulerConfiguration configuration) {
        this.tokenStream = tokenStream;
        this.configuration = configuration;
        this.parseContext = new ParseContext(configuration);
        this.parseContext.setTypeCheckVisitor(this.configuration.getTypeCheckVisitor());
        this.parseContext.setRoot(true);

        // 注册所有原子解析器
        registerParsers();
    }

    private void registerParsers() {
        // 注册语句级别解析器
        statementParsers.add(Parsers.VAR_PARSER);
        statementParsers.add(Parsers.RETURN_PARSER);
        statementParsers.add(Parsers.BREAK_PARSER);
        statementParsers.add(Parsers.CONTINUE_PARSER);
        statementParsers.add(Parsers.RULE_PARSER);
        statementParsers.add(Parsers.INFIX_PARSER);
        // 故意从 statementParsers 中移除 BLOCK_PARSER 和 RSON_PARSER：
        // 它们将在 parseExpression 中作为基本表达式处理，
        // 这样顶层的 RSON/块字面量后面才能正确跟中缀运算符。

        // 以下解析器既是语句解析器，也是表达式解析器
        statementParsers.add(Parsers.IF_PARSER);
        statementParsers.add(Parsers.WHILE_PARSER);
        statementParsers.add(Parsers.FOR_EACH_PARSER);
        statementParsers.add(Parsers.FUN_PARSER);
        statementParsers.add(Parsers.MATCH_PARSER);

        // 表达式级别解析器
        expressionParsers.add(Parsers.IDENTIFIER_PARSER);
        expressionParsers.add(Parsers.BOOL_PARSER);
        expressionParsers.add(Parsers.PAREN_PARSER);
        expressionParsers.add(Parsers.UNARY_EXPRESSION_PARSER);
        expressionParsers.add(Parsers.NUMBER_PARSER);
        expressionParsers.add(Parsers.STRING_PARSER);
        expressionParsers.add(Parsers.NULL_PARSER);
        expressionParsers.add(Parsers.ARRAY_PARSER);
        expressionParsers.add(Parsers.RSON_PARSER);
        expressionParsers.add(Parsers.BLOCK_PARSER);
        expressionParsers.add(Parsers.TYPE_OF_PARSER);

        // 这些既是语句也可以是表达式的解析器也需要添加到表达式解析器列表中
        expressionParsers.add(Parsers.IF_PARSER);
        expressionParsers.add(Parsers.WHILE_PARSER);
        expressionParsers.add(Parsers.FOR_EACH_PARSER);
        expressionParsers.add(Parsers.FUN_PARSER);
        expressionParsers.add(Parsers.MATCH_PARSER);
    }

    @Override
    public ImportNode parseImport() {
        // 导入语句解析逻辑
        if (tokenStream.token().type != TokenType.KEY_IMPORT) {
            throw new IllegalArgumentException(
                    "Expected KEY_IMPORT token, but got " + tokenStream.token().type);
        }
        Token importToken = tokenStream.token();
        tokenStream.nextToken();

        boolean hasImportInfix = false;
        if (tokenStream.token().type == TokenType.KEY_INFIX) {
            hasImportInfix = true;
            tokenStream.nextToken();
        }

        AssertUtil.assertToken(tokenStream, TokenType.STRING);
        String path = tokenStream.token().name;
        tokenStream.nextToken();

        Token aliasToken = null;
        if (tokenStream.token().type == TokenType.IDENTIFIER) {
            aliasToken = tokenStream.token();
            tokenStream.nextToken();
        }

        // 不允许出现无别名切无中缀标识的导入语句
        if (aliasToken == null && !hasImportInfix) {
            throw new SyntaxException(
                    "import statement without alias and infix is not allowed",
                    importToken.location);
        }
        if (tokenStream.token().type == TokenType.SEMICOLON) {
            tokenStream.nextToken();
        } else if (!tokenStream.isNewLine() && tokenStream.token().type != TokenType.EOF) {
            throw new SyntaxException("expected semicolon or newline after import statement", tokenStream.token().location);
        }
        return new ImportNode(path, aliasToken == null ? null : aliasToken.name, hasImportInfix,
                importToken.location);
    }

    @Override
    public BaseNode parseStatement() {
        Token token = tokenStream.token();
        boolean isRoot = parseContext.isRoot();
        if (token.type == TokenType.KEY_RULE || token.type == TokenType.KEY_INFIX) {
            if (!isRoot) {
                throw new UnsupportedOperationException();
            }
        }
        if (isRoot) {
            parseContext.setRoot(false);
        }

        // 查找适合的语句解析器并执行解析
        BaseNode statement = null;
        boolean isNeedSemicolon = true;

        for (AtomParser parser : statementParsers) {
            if (parser.support(tokenStream)) { // 使用整个tokenStream代替单个token
                statement = parser.parse(this);
                // 根据具体解析器类型确定是否需要分号
                isNeedSemicolon = needSemicolon(parser);
                break;
            }
        }

        if (statement == null) {
            statement = parseExpression();
        }

        if (isNeedSemicolon) {
            if (tokenStream.token().type == TokenType.SEMICOLON) {
                tokenStream.nextToken();
            } else if (!tokenStream.isNewLine() && tokenStream.token().type != TokenType.EOF
                    && tokenStream.token().type != TokenType.RIGHT_BRACE) {
                throw new SyntaxException("expected semicolon or newline after statement",
                        tokenStream.token().location);
            }
        }

        if (isRoot) {
            parseContext.setRoot(true);
        }

        if (statement == null) {
            throw new SyntaxException("unknown expression start '" + token.name + "'", token.location);
        }

        return statement;
    }

    // 判断特定解析器是否需要分号
    private boolean needSemicolon(AtomParser parser) {
        return !(parser instanceof IfParser || parser instanceof WhileParser
                || parser instanceof ForEachParser || parser instanceof FunParser
                || parser instanceof RuleParser || parser instanceof InfixParser
                || parser instanceof BlockParser || parser instanceof MatchParser);
    }

    @Override
    public BaseNode parseExpression() {
        BaseNode lhs = parsePrimaryExpression();
        if (lhs == null) {
            Token token = tokenStream.token();
            throw new SyntaxException("unknown expression start '" + token.name + "'", token.location);
        }
        return parseBinaryNode(0, lhs);
    }

    public BaseNode parseBinaryNode(int expPrec, BaseNode lhs) {
        while (true) {
            Token curOpToken = tokenStream.token();
            if (curOpToken.type == TokenType.PIPE) {
                throw new SyntaxException(
                        "\"|\" is only allowed in pattern matching",
                        curOpToken.location);
            }
            int curTokenProc = OperationDefine.findPrecedence(curOpToken.type);
            if (curTokenProc < expPrec) {
                return lhs;
            }
            if (curOpToken.type == TokenType.IDENTIFIER && tokenStream.isNewLine()) {
                return lhs;
            }

            tokenStream.nextToken();
            BaseNode rhs = parsePrimaryExpression();
            Objects.requireNonNull(rhs);

            Token nextToken = tokenStream.token();
            int nextTokenProc = OperationDefine.findPrecedence(nextToken.type);
            if (curTokenProc < nextTokenProc) {
                rhs = parseBinaryNode(curTokenProc + 1, rhs);
                Objects.requireNonNull(rhs);
            }

            if (curOpToken.type == TokenType.ASSIGN) {
                if (!(lhs instanceof IndexNode) && !(lhs instanceof NameNode)
                        && !(lhs instanceof DotNode)) {
                    throw new SyntaxException("invalid assignment target\t token=" + curOpToken);
                }
                lhs = new AssignNode(lhs, rhs, null, lhs.getLocation());
            } else {
                BinaryOperation operation = this.configuration.getBinaryOperationFactory()
                        .findOperation(curOpToken.type.name());
                Objects.requireNonNull(operation);
                lhs = new BinaryOperationNode(curOpToken.type, curOpToken.name, lhs, rhs, operation,
                        lhs.getLocation());
            }
        }
    }

    public BaseNode parsePrimaryExpression() {
        BaseNode node = null;
        // 使用表达式解析器解析基本表达式
        for (AtomParser parser : expressionParsers) {
            if (parser.support(tokenStream)) { // 使用整个tokenStream代替单个token
                node = parser.parse(this);
                break;
            }
        }

        // 如果没有找到合适的解析器，返回null
        if (node == null) {
            return null;
        }

        // 处理链式调用，如 a.b(), a[b], a.b 等
        while (tokenStream.token().type == TokenType.DOT
                || tokenStream.token().type == TokenType.LEFT_PAREN
                || tokenStream.token().type == TokenType.LEFT_SQUARE) {
            if (tokenStream.token().type == TokenType.LEFT_PAREN) {
                // 函数调用
                tokenStream.nextToken();
                List<BaseNode> params = new ArrayList<>();
                if (tokenStream.token().type != TokenType.RIGHT_PAREN) {
                    params.add(parseExpression());
                }
                while (tokenStream.token().type != TokenType.RIGHT_PAREN) {
                    AssertUtil.assertToken(tokenStream, TokenType.COMMA);
                    tokenStream.nextToken();
                    params.add(parseExpression());
                }
                AssertUtil.assertToken(tokenStream, TokenType.RIGHT_PAREN);
                tokenStream.nextToken();

                BinaryOperation callOperation = configuration.getBinaryOperationFactory()
                        .findOperation(TokenType.CALL.name());
                node = new CallNode(node, null, params, callOperation, node.getLocation());
            } else if (tokenStream.token().type == TokenType.LEFT_SQUARE) {
                // 数组索引
                tokenStream.nextToken();

                BaseNode indexNode = parseExpression();
                Objects.requireNonNull(indexNode);

                AssertUtil.assertToken(tokenStream, TokenType.RIGHT_SQUARE);
                tokenStream.nextToken();

                BinaryOperation indexOperation = configuration.getBinaryOperationFactory()
                        .findOperation(TokenType.INDEX.name());
                node = new IndexNode(node, indexNode, indexOperation, node.getLocation());
            } else {
                // 对象属性访问
                tokenStream.nextToken();
                Token dotNameNode = tokenStream.token();
                AssertUtil.assertToken(dotNameNode, TokenType.IDENTIFIER);
                // only identifiers are supported for dot call
                BaseNode nameNode = identifierParser.parse(this);

                BinaryOperation dotOperation = configuration.getBinaryOperationFactory()
                        .findOperation(TokenType.DOT.name());
                node = new DotNode(node, nameNode, dotOperation, node.getLocation());
            }
        }
        return node;
    }

    // 各种getter方法，供原子解析器使用
    public TokenStream getTokenStream() {
        return tokenStream;
    }

    public ParseContext getParseContext() {
        return parseContext;
    }

    public RulerConfiguration getConfiguration() {
        return configuration;
    }

    // 是否处于循环内
    public boolean isInLoop() {
        return parseContext.isInLoop();
    }

    // 设置是否在循环内
    public void setInLoop(boolean inLoop) {
        parseContext.setInLoop(inLoop);
    }
}
