package com.kamijoucen.ruler.compiler.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.ImportNode;
import com.kamijoucen.ruler.ast.factor.BinaryOperationNode;
import com.kamijoucen.ruler.ast.factor.NameNode;
import com.kamijoucen.ruler.ast.factor.OutNameNode;
import com.kamijoucen.ruler.common.MessageType;
import com.kamijoucen.ruler.common.OperationDefine;
import com.kamijoucen.ruler.compiler.Parser;
import com.kamijoucen.ruler.compiler.TokenStream;
import com.kamijoucen.ruler.compiler.impl.ParseContext;
import com.kamijoucen.ruler.compiler.parser.impl.*;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.operation.BinaryOperation;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.util.AssertUtil;

public class AtomParserManager implements Parser {

    private final List<AtomParser> statementParsers = new ArrayList<>();
    private final List<AtomParser> expressionParsers = new ArrayList<>();
    private final TokenStream tokenStream;
    private final ParseContext parseContext;
    private final RulerConfiguration configuration;

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
        statementParsers.add(new VarParser());
        statementParsers.add(new ReturnParser());
        statementParsers.add(new BreakParser());
        statementParsers.add(new ContinueParser());
        statementParsers.add(new RuleParser());
        statementParsers.add(new InfixParser());
        statementParsers.add(new BlockParser());

        // 以下解析器既是语句解析器，也是表达式解析器
        IfParser ifParser = new IfParser();
        WhileParser whileParser = new WhileParser();
        ForEachParser forEachParser = new ForEachParser();
        FunParser funParser = new FunParser();

        statementParsers.add(ifParser);
        statementParsers.add(whileParser);
        statementParsers.add(forEachParser);
        statementParsers.add(funParser);

        // 表达式级别解析器
        expressionParsers.add(new IdentifierParser());
        expressionParsers.add(new BoolParser());
        expressionParsers.add(new ParenParser());
        expressionParsers.add(new UnaryExpressionParser());
        expressionParsers.add(new NumberParser());
        expressionParsers.add(new StringParser());
        expressionParsers.add(new NullParser());
        expressionParsers.add(new ArrayParser());
        expressionParsers.add(new RsonParser());
        expressionParsers.add(new TypeOfParser());
        expressionParsers.add(new ThisParser());

        // 这些既是语句也可以是表达式的解析器也需要添加到表达式解析器列表中
        expressionParsers.add(ifParser);
        expressionParsers.add(whileParser);
        expressionParsers.add(forEachParser);
        expressionParsers.add(funParser);
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
            throw SyntaxException.withSyntax(
                    "import statement without alias and infix is not allowed",
                    importToken.location);
        }
        AssertUtil.assertToken(tokenStream, TokenType.SEMICOLON);
        tokenStream.nextToken();
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
            if (parser.support(tokenStream)) {  // 使用整个tokenStream代替单个token
                statement = parser.parse(this);
                // 根据具体解析器类型确定是否需要分号
                isNeedSemicolon = needSemicolon(parser);
                break;
            }
        }

        // 如果没有找到适合的语句解析器，则尝试作为表达式解析
        if (statement == null) {
            statement = parseExpression();
        }

        if (isNeedSemicolon) {
            AssertUtil.assertToken(tokenStream, TokenType.SEMICOLON);
            tokenStream.nextToken();
        }

        if (isRoot) {
            parseContext.setRoot(true);
        }

        if (statement == null) {
            String message = configuration.getMessageManager()
                    .buildMessage(MessageType.UNKNOWN_EXPRESSION_START, token.location, token.name);
            throw new SyntaxException(message);
        }

        return statement;
    }

    // 判断特定解析器是否需要分号
    private boolean needSemicolon(AtomParser parser) {
        // 结构化语句不需要分号，如if、while、for、fun，后续可以通过更优雅的方式确定
        return !(parser instanceof IfParser ||
                parser instanceof WhileParser ||
                parser instanceof ForEachParser ||
                parser instanceof FunParser ||
                parser instanceof RuleParser ||
                parser instanceof InfixParser ||
                parser instanceof BlockParser);
    }

    @Override
    public BaseNode parseExpression() {
        BaseNode lhs = parsePrimaryExpression();
        if (lhs == null) {
            Token token = tokenStream.token();
            String message = configuration.getMessageManager()
                    .buildMessage(MessageType.UNKNOWN_EXPRESSION_START, token.location, token.name);
            throw new SyntaxException(message);
        }
        return parseBinaryNode(0, lhs);
    }

    public BaseNode parseBinaryNode(int expPrec, BaseNode lhs) {
        while (true) {
            Token curOpToken = tokenStream.token();
            int curTokenProc = OperationDefine.findPrecedence(curOpToken.type);
            if (curTokenProc < expPrec) {
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
                if (!(lhs instanceof com.kamijoucen.ruler.ast.expression.IndexNode) &&
                    !(lhs instanceof NameNode) &&
                    !(lhs instanceof com.kamijoucen.ruler.ast.expression.DotNode)) {
                    throw SyntaxException.withSyntax("非法的赋值操作", curOpToken);
                }
                lhs = new com.kamijoucen.ruler.ast.expression.AssignNode(lhs, rhs, null, lhs.getLocation());
            } else {
                BinaryOperation operation = this.configuration.getBinaryOperationFactory()
                        .findOperation(curOpToken.type.name());
                Objects.requireNonNull(operation);
                lhs = new BinaryOperationNode(
                        curOpToken.type, curOpToken.name, lhs, rhs, operation, lhs.getLocation());
            }
        }
    }

    // 为原子解析器提供的辅助方法
    public BaseNode parseIdentifier() {
        Token token = tokenStream.token();
        BaseNode nameNode;
        if (token.type == TokenType.IDENTIFIER) {
            nameNode = new NameNode(token, token.location);
        } else if (token.type == TokenType.OUT_IDENTIFIER) {
            nameNode = new OutNameNode(token, token.location);
        } else {
            String message = configuration.getMessageManager()
                    .buildMessage(MessageType.ILLEGAL_IDENTIFIER, token.location, token.name);
            throw new SyntaxException(message);
        }
        tokenStream.nextToken();
        return nameNode;
    }

    public BaseNode parsePrimaryExpression() {
        BaseNode node = null;
        // 使用表达式解析器解析基本表达式
        for (AtomParser parser : expressionParsers) {
            if (parser.support(tokenStream)) {  // 使用整个tokenStream代替单个token
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
                node = new com.kamijoucen.ruler.ast.expression.CallNode(node, null, params, callOperation, node.getLocation());
            } else if (tokenStream.token().type == TokenType.LEFT_SQUARE) {
                // 数组索引
                tokenStream.nextToken();

                BaseNode indexNode = parseExpression();
                Objects.requireNonNull(indexNode);

                AssertUtil.assertToken(tokenStream, TokenType.RIGHT_SQUARE);
                tokenStream.nextToken();

                BinaryOperation indexOperation = configuration.getBinaryOperationFactory()
                        .findOperation(TokenType.INDEX.name());
                node = new com.kamijoucen.ruler.ast.expression.IndexNode(node, indexNode, indexOperation, node.getLocation());
            } else {
                // 对象属性访问
                tokenStream.nextToken();
                Token dotNameNode = tokenStream.token();
                AssertUtil.assertToken(dotNameNode, TokenType.IDENTIFIER);
                // only identifiers are supported for dot call
                BaseNode nameNode = parseIdentifier();

                BinaryOperation dotOperation = configuration.getBinaryOperationFactory()
                        .findOperation(TokenType.DOT.name());
                node = new com.kamijoucen.ruler.ast.expression.DotNode(node, nameNode, dotOperation, node.getLocation());
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