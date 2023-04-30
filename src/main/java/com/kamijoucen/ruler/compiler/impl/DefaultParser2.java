package com.kamijoucen.ruler.compiler.impl;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.expression.*;
import com.kamijoucen.ruler.ast.facotr.*;
import com.kamijoucen.ruler.common.OperationDefine;
import com.kamijoucen.ruler.compiler.Parser;
import com.kamijoucen.ruler.compiler.TokenStream;
import com.kamijoucen.ruler.config.RulerConfiguration;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.operation.UnaryAddOperation;
import com.kamijoucen.ruler.operation.UnarySubOperation;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;
import com.kamijoucen.ruler.type.ArrayType;
import com.kamijoucen.ruler.type.UnknownType;
import com.kamijoucen.ruler.util.AssertUtil;
import com.kamijoucen.ruler.util.CollectionUtil;
import com.kamijoucen.ruler.util.IOUtil;
import com.kamijoucen.ruler.value.BaseValue;

import java.util.*;

public class DefaultParser2 implements Parser {

    private final RulerConfiguration configuration;

    private final ParseContext parseContext;

    private final List<BaseNode> statements = new ArrayList<>();

    private final TokenStream tokenStream;

    public DefaultParser2(TokenStream tokenStream, RulerConfiguration configuration) {
        this.tokenStream = tokenStream;
        this.configuration = configuration;
        this.parseContext = new ParseContext(this.configuration);
        this.parseContext.setTypeCheckVisitor(this.configuration.getTypeCheckVisitor());
        this.parseContext.setRoot(true);
    }

    @Override
    public List<BaseNode> parse() {
        return null;
    }

    @Override
    public ImportNode parseImport() {
        return null;
    }

    @Override
    public BaseNode parseStatement() {
        Token token = tokenStream.token();
        BaseNode statement = null;

        boolean isRoot = parseContext.isRoot();
        if (token.type == TokenType.KEY_RULE
                || token.type == TokenType.KEY_INFIX) {
            if (!isRoot) {
                throw new UnsupportedOperationException();
            }
        }
        if (isRoot) {
            parseContext.setRoot(false);
        }
        switch (token.type) {
            case KEY_RETURN:
                statement = parseReturn();
                break;
            case KEY_BREAK:
                statement = parseBreak();
                break;
            case KEY_CONTINUE:
                statement = parseContinue();
                break;
            case KEY_VAR:
                statement = parseVariableDefine();
                break;
            case KEY_RULE:
                statement = parseRuleBlock();
                break;
            case KEY_INFIX:
                statement = parseInfixDefinitionNode();
                break;
            default:
                statement = parseExpression();
        }
        if (isRoot) {
            parseContext.setRoot(true);
        }
        if (statement == null) {
            throw SyntaxException.withSyntax("error statement");
        }
        return statement;
    }

    @Override
    public BaseNode parseExpression() {
        BaseNode lhs = parsePrimaryExpression();
        Objects.requireNonNull(lhs);
        return parseBinaryNode(0, lhs);
    }

    public BaseNode parseVariableDefine() {
        Token varToken = tokenStream.token();
        // eat var
        AssertUtil.assertToken(varToken, TokenType.KEY_VAR);
        tokenStream.nextToken();
        AssertUtil.assertToken(tokenStream, TokenType.IDENTIFIER);

        BaseNode nameNode = parseIdentifier();
        Objects.requireNonNull(nameNode);

        AssertUtil.assertToken(tokenStream, TokenType.ASSIGN);
        tokenStream.nextToken();

        BaseNode expNode = this.parseExpression();
        Objects.requireNonNull(expNode);

        return new VariableDefineNode(nameNode, expNode, varToken.location);
    }

    // var name = name.arr()[5].ToString()[1];

    // name .arr

    // arr()
    // 1 + 2 * name()
    // name() + 1
    public BaseNode parseBinaryNode(int expPrec, BaseNode lhs) {
        while (true) {
            Token currentToken = tokenStream.token();
            if (currentToken.type == TokenType.ASSIGN) {
                BaseNode rhs = parseExpression();
                Objects.requireNonNull(rhs);
                lhs = new AssignNode2(lhs, rhs, lhs.getLocation());
            } else if (currentToken.type == TokenType.DOT) {
                tokenStream.nextToken();
                Token dotNameNode = tokenStream.token();
                AssertUtil.assertToken(dotNameNode, TokenType.IDENTIFIER);
                tokenStream.nextToken();
                // only identifiers are supported for dot call
                BaseNode nameNode = parseIdentifier();
                lhs = new DotNode2(lhs, nameNode, lhs.getLocation());
            } else if (currentToken.type == TokenType.LEFT_PAREN) {
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
                lhs = new CallNode2(lhs, null, params, lhs.getLocation());
            } else if (currentToken.type == TokenType.LEFT_SQUARE) {
                tokenStream.nextToken();
                BaseNode indexNode = parseExpression();
                Objects.requireNonNull(indexNode);
                lhs = new IndexNode2(lhs, indexNode, lhs.getLocation());
            } else {
                int curTokenProc = OperationDefine.findPrecedence(currentToken.type);
                if (curTokenProc < expPrec) {
                    return lhs;
                }
                tokenStream.nextToken();
                BaseNode rhs = parsePrimaryExpression();
                Objects.requireNonNull(rhs);

                Token nextToken = tokenStream.token();
                int nextTokenProc = OperationDefine.findPrecedence(nextToken.type);
                if (nextTokenProc == -1) {
                    return rhs;
                }
                if (curTokenProc < nextTokenProc) {
                    rhs = parseBinaryNode(curTokenProc + 1, rhs);
                    Objects.requireNonNull(rhs);
                }
                lhs = new BinaryOperationNode(currentToken.type, currentToken.name, lhs, rhs, lhs.getLocation());
            }
        }
    }

    public BaseNode parsePrimaryExpression() {
        Token token = tokenStream.token();
        switch (token.type) {
            case IDENTIFIER:
            case OUT_IDENTIFIER:
                return parseIdentifier();
            case KEY_THIS:
                return parseThis();
            case KEY_FALSE:
            case KEY_TRUE:
                return parseBool();
            case LEFT_PAREN:
                return parseParen();
            case ADD:
            case SUB:
            case NOT:
                return parseUnaryExpression();
            case INTEGER:
            case DOUBLE:
                return parseNumber();
            case STRING:
                return parseString();
            case KEY_FUN:
                return parseFunDefine();
            case KEY_NULL:
                return parseNull();
            case LEFT_SQUARE:
                return parseArray();
            case LEFT_BRACE:
                return parseRsonNode();
            case KEY_TYPEOF:
                return parseTypeOfNode();
            case KEY_IF:
                return parseIfStatement();
            case KEY_FOR:
                return parseForEachStatement();
            case KEY_WHILE:
                return parseWhileStatement();
            default:
                throw SyntaxException.withSyntax("unkown token:" + token);
        }
    }

    public BaseNode parseIdentifier() {
        Token token = tokenStream.token();
        BaseNode nameNode = null;
        if (token.type == TokenType.IDENTIFIER) {
            nameNode = new NameNode(token, token.location);
        } else if (token.type == TokenType.OUT_IDENTIFIER) {
            nameNode = new OutNameNode(token, token.location);
        } else {
            throw SyntaxException.withSyntax("error identifier: " + token);
        }
        tokenStream.nextToken();
        return nameNode;
    }

    public BaseNode parseBlock() {
        Token lToken = tokenStream.token();
        AssertUtil.assertToken(lToken, TokenType.LEFT_BRACE);
        tokenStream.nextToken();
        List<BaseNode> blocks = new ArrayList<>();
        while (tokenStream.token().type != TokenType.EOF
                && tokenStream.token().type != TokenType.RIGHT_BRACE) {
            blocks.add(parseStatement());
        }

        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_BRACE);
        tokenStream.nextToken();
        if (isLoop) {
            return new LoopBlockNode(blocks, lToken.location);
        } else {
            return new BlockNode(blocks, lToken.location);
        }
    }

    public BaseNode parseWhileStatement() {
        Token whileToken = tokenStream.token();
        AssertUtil.assertToken(whileToken, TokenType.KEY_WHILE);
        tokenStream.nextToken();
        BaseNode condition = parseExpression();
        BaseNode blockAST;
        if (tokenStream.token().type == TokenType.LEFT_BRACE) {
            blockAST = parseBlock();
        } else if (tokenStream.token().type == TokenType.COLON) {
            tokenStream.nextToken();
            BaseNode statement = parseStatement();
            blockAST = new LoopBlockNode(Collections.singletonList(statement), statement.getLocation());
        } else {
            throw SyntaxException.withSyntax("while condition expression expected ':' or '{'", tokenStream.token());
        }
        return new WhileStatementNode(condition, blockAST, whileToken.location);
    }

    public BaseNode parseUnaryExpression() {
        Token token = tokenStream.token();
        tokenStream.nextToken();
        if (token.type == TokenType.ADD || token.type == TokenType.SUB) {
            return new UnaryOperationNode(token.type, parsePrimaryExpression(),
                    token.type == TokenType.ADD ? new UnaryAddOperation() : new UnarySubOperation(), token.location);
        } else if (token.type == TokenType.NOT) {
            return new LogicBinaryOperationNode(TokenType.NOT, parsePrimaryExpression(), null,
                    OperationDefine.findLogicOperation(TokenType.NOT), token.location);
        } else {
            throw SyntaxException.withSyntax("Unsupported unary operator:" + token);
        }
    }

    public BaseNode parseIfStatement() {

        Token ifToken = tokenStream.token();

        AssertUtil.assertToken(ifToken, TokenType.KEY_IF);
        tokenStream.nextToken();

        BaseNode condition = parseExpression();
        BaseNode thenBlock = null;

        if (tokenStream.token().type == TokenType.LEFT_BRACE) {
            thenBlock = parseBlock();
        } else if (tokenStream.token().type == TokenType.COLON) {
            tokenStream.nextToken();
            BaseNode statement = parseStatement();
            thenBlock = new BlockNode(Collections.singletonList(statement), statement.getLocation());
        } else {
            throw SyntaxException.withSyntax("if condition expression expected ':' or '{'", tokenStream.token());
        }

        BaseNode elseBlock = null;
        if (tokenStream.token().type == TokenType.KEY_ELSE) {
            Token token = tokenStream.nextToken();
            if (token.type == TokenType.LEFT_BRACE) {
                elseBlock = parseBlock();
            } else {
                BaseNode statement = parseStatement();
                elseBlock = new BlockNode(Collections.singletonList(statement), statement.getLocation());
            }
        }
        return new IfStatementNode(condition, thenBlock, elseBlock, ifToken.location);
    }

    public BaseNode parseFunDefine() {
        Token funToken = tokenStream.token();
        // eat fun
        AssertUtil.assertToken(tokenStream, TokenType.KEY_FUN);
        tokenStream.nextToken();
        String name = null;
        if (tokenStream.token().type == TokenType.IDENTIFIER) {
            name = tokenStream.token().name;
            tokenStream.nextToken();
        }
        // eat (
        AssertUtil.assertToken(tokenStream, TokenType.LEFT_PAREN);
        tokenStream.nextToken();
        List<BaseNode> param = new ArrayList<BaseNode>();
        if (tokenStream.token().type != TokenType.RIGHT_PAREN) {
            AssertUtil.assertToken(tokenStream, TokenType.IDENTIFIER);
            param.add(parseIdentifier());
        }
        while (tokenStream.token().type != TokenType.RIGHT_PAREN) {
            AssertUtil.assertToken(tokenStream, TokenType.COMMA);
            tokenStream.nextToken();
            AssertUtil.assertToken(tokenStream, TokenType.IDENTIFIER);
            param.add(parseIdentifier());
        }
        // eat )
        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_PAREN);
        tokenStream.nextToken();

        if (tokenStream.token().type == TokenType.ARROW) {
            tokenStream.nextToken();
            BaseNode exp = parseExpression();

            BaseNode returnNode = new ReturnNode(CollectionUtil.list(exp), exp.getLocation());
            BlockNode blockNode = new BlockNode(CollectionUtil.list(returnNode), exp.getLocation());
            return new ClosureDefineNode(name, param, blockNode, funToken.location);
        } else {
            BaseNode block = parseBlock();
            return new ClosureDefineNode(name, param, block, funToken.location);
        }
    }

    public BaseNode parseForEachStatement() {
        // key
        Token forToken = tokenStream.token();
        AssertUtil.assertToken(forToken, TokenType.KEY_FOR);
        tokenStream.nextToken();
        // temp var
        AssertUtil.assertToken(tokenStream, TokenType.IDENTIFIER);
        Token name = tokenStream.token();
        tokenStream.nextToken();
        AssertUtil.assertToken(tokenStream, TokenType.KEY_IN);
        tokenStream.nextToken();
        BaseNode arrayExp = parseExpression();
        // type check
        BaseValue typeCheckValue = arrayExp.typeCheck(runtimeContext, null);
        if (typeCheckValue.getType() != UnknownType.INSTANCE.getType()
                && typeCheckValue.getType() != ArrayType.INSTANCE.getType()) {
            throw SyntaxException.withSyntax("The value of the expression must be an array!");
        }
        BaseNode blockNode = null;
        if (tokenStream.token().type == TokenType.LEFT_BRACE) {
            blockNode = parseBlock();
        } else if (tokenStream.token().type == TokenType.COLON) {
            tokenStream.nextToken();
            BaseNode statement = parseStatement();
            blockNode = new LoopBlockNode(Collections.singletonList(statement), statement.getLocation());
        } else {
            throw SyntaxException.withSyntax("for condition expression expected ':' or '{'", tokenStream.token());
        }
        return new ForEachStatementNode(name, arrayExp, blockNode, forToken.location);
    }

    public BaseNode parseParen() {
        AssertUtil.assertToken(tokenStream, TokenType.LEFT_PAREN);
        tokenStream.nextToken();
        BaseNode ast = parseExpression();
        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_PAREN);
        tokenStream.nextToken();
        return ast;
    }

    public BaseNode parseNumber() {
        Token token = tokenStream.token();
        tokenStream.nextToken();
        if (token.type == TokenType.INTEGER) {
            return new IntegerNode(Long.parseLong(token.name), token.location);
        }
        if (token.type == TokenType.DOUBLE) {
            return new DoubleNode(Double.parseDouble(token.name), token.location);
        }
        throw SyntaxException.withSyntax("需要一个数字", token);
    }

    public BaseNode parseString() {
        AssertUtil.assertToken(tokenStream, TokenType.STRING);
        Token token = tokenStream.token();
        tokenStream.nextToken();
        return new StringNode(token.name, token.location);
    }

    public BaseNode parseBool() {
        Token token = tokenStream.token();
        if (token.type != TokenType.KEY_FALSE && token.type != TokenType.KEY_TRUE) {
            throw SyntaxException.withSyntax("需要一个bool", token);
        }
        tokenStream.nextToken();
        return new BoolNode(Boolean.parseBoolean(token.name), token.location);
    }

    public BaseNode parseNull() {
        AssertUtil.assertToken(tokenStream, TokenType.KEY_NULL);
        Token token = tokenStream.token();
        tokenStream.nextToken();
        return new NullNode(token.location);
    }

    public BaseNode parseContinue() {
        AssertUtil.assertToken(tokenStream, TokenType.KEY_CONTINUE);
        Token token = tokenStream.token();
        tokenStream.nextToken();
        return new ContinueNode(token.location);
    }

    public BaseNode parseBreak() {
        AssertUtil.assertToken(tokenStream, TokenType.KEY_BREAK);
        Token token = tokenStream.token();
        tokenStream.nextToken();
        return new BreakNode(token.location);
    }

    public BaseNode parseReturn() {
        AssertUtil.assertToken(tokenStream, TokenType.KEY_RETURN);
        Token returnToken = tokenStream.token();
        tokenStream.nextToken();

        List<BaseNode> param = new ArrayList<BaseNode>();
        if (tokenStream.token().type != TokenType.SEMICOLON) {
            param.add(parseExpression());
        }
        while (tokenStream.token().type != TokenType.SEMICOLON) {
            AssertUtil.assertToken(tokenStream, TokenType.COMMA);
            tokenStream.nextToken();
            param.add(parseExpression());
        }
        return new ReturnNode(param, returnToken.location);
    }

    public BaseNode parseArray() {
        AssertUtil.assertToken(tokenStream, TokenType.LEFT_SQUARE);
        Token lToken = tokenStream.token();
        tokenStream.nextToken();
        List<BaseNode> arrValues = new ArrayList<BaseNode>();
        if (tokenStream.token().type != TokenType.RIGHT_SQUARE) {
            arrValues.add(parseExpression());
        }
        while (tokenStream.token().type != TokenType.RIGHT_SQUARE) {
            AssertUtil.assertToken(tokenStream, TokenType.COMMA);
            tokenStream.nextToken();
            arrValues.add(parseExpression());
        }
        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_SQUARE);
        tokenStream.nextToken();
        return new ArrayNode(arrValues, lToken.location);
    }

    public BaseNode parseTypeOfNode() {
        AssertUtil.assertToken(tokenStream, TokenType.KEY_TYPEOF);
        Token typeOfToken = tokenStream.token();
        tokenStream.nextToken();
        AssertUtil.assertToken(tokenStream, TokenType.LEFT_PAREN);
        tokenStream.nextToken();
        BaseNode exp = parseExpression();
        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_PAREN);
        tokenStream.nextToken();
        return new TypeOfNode(exp, typeOfToken.location);
    }

    public BaseNode parseRsonNode() {
        AssertUtil.assertToken(tokenStream, TokenType.LEFT_BRACE);
        Token lToken = tokenStream.token();
        tokenStream.nextToken();
        Map<String, BaseNode> properties = new HashMap<String, BaseNode>();
        if (tokenStream.token().type != TokenType.RIGHT_BRACE) {
            if (tokenStream.token().type != TokenType.IDENTIFIER
                    && tokenStream.token().type != TokenType.STRING) {
                throw SyntaxException.withSyntax("无效的key", tokenStream.token());
            }
            Token name = tokenStream.token();
            tokenStream.nextToken();

            AssertUtil.assertToken(tokenStream, TokenType.COLON);
            tokenStream.nextToken();
            properties.put(name.name, parseExpression());
        }
        while (tokenStream.token().type != TokenType.RIGHT_BRACE) {
            AssertUtil.assertToken(tokenStream, TokenType.COMMA);
            tokenStream.nextToken();

            if (tokenStream.token().type == TokenType.RIGHT_BRACE) {
                break;
            }
            if (tokenStream.token().type != TokenType.IDENTIFIER
                    && tokenStream.token().type != TokenType.STRING) {
                throw SyntaxException.withSyntax("无效的key", tokenStream.token());
            }
            Token name = tokenStream.token();
            tokenStream.nextToken();
            AssertUtil.assertToken(tokenStream, TokenType.COLON);
            tokenStream.nextToken();
            properties.put(name.name, parseExpression());
        }
        AssertUtil.assertToken(tokenStream, TokenType.RIGHT_BRACE);
        tokenStream.nextToken();
        return new RsonNode(properties, lToken.location);
    }

    public BaseNode parseThis() {
        AssertUtil.assertToken(tokenStream, TokenType.KEY_THIS);
        Token thisToken = tokenStream.token();
        tokenStream.nextToken();
        return new ThisNode(thisToken.location);
    }

    public BaseNode parseRuleBlock() {
        AssertUtil.assertToken(tokenStream, TokenType.KEY_RULE);
        Token ruleToken = tokenStream.token();
        tokenStream.nextToken();

        AssertUtil.assertToken(tokenStream, TokenType.STRING);
        Token nameToken = tokenStream.token();
        tokenStream.nextToken();

        BaseNode blockNode = parseBlock();
        return new RuleStatementNode(
                new StringNode(nameToken.name, nameToken.location),
                (BlockNode) blockNode,
                ruleToken.location);
    }

    public BaseNode parseInfixDefinitionNode() {
        AssertUtil.assertToken(tokenStream, TokenType.KEY_INFIX);
        Token infixToken = tokenStream.token();
        // eat the infix token
        tokenStream.nextToken();
        // infix operation
        ClosureDefineNode functionNode = (ClosureDefineNode) parseFunDefine();

        String infixName = functionNode.getName();
        if (IOUtil.isBlank(infixName)) {
            throw SyntaxException.withSyntax("infix function name is blank!", infixToken);
        }
        return new InfixDefinitionNode(functionNode, infixToken.location);
    }

}
