package com.kamijoucen.ruler.compiler.parser.impl;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.factor.UnaryOperationNode;
import com.kamijoucen.ruler.compiler.TokenStream;
import com.kamijoucen.ruler.compiler.parser.AtomParser;
import com.kamijoucen.ruler.compiler.parser.AtomParserManager;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.operation.BinaryOperation;
import com.kamijoucen.ruler.operation.UnaryAddOperation;
import com.kamijoucen.ruler.operation.UnarySubOperation;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;

import java.util.Objects;

/**
 * 一元运算符解析器（如+, -, !）
 */
public class UnaryExpressionParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.ADD ||
               tokenStream.token().type == TokenType.SUB ||
               tokenStream.token().type == TokenType.NOT;
    }

    @Override
    public BaseNode parse(AtomParserManager manager) {
        TokenStream tokenStream = manager.getTokenStream();
        Token token = tokenStream.token();
        tokenStream.nextToken();

        if (token.type == TokenType.ADD || token.type == TokenType.SUB) {
            return new UnaryOperationNode(
                token.type,
                manager.parsePrimaryExpression(),
                token.type == TokenType.ADD ? new UnaryAddOperation() : new UnarySubOperation(),
                token.location
            );
        } else if (token.type == TokenType.NOT) {
            BinaryOperation operation = manager.getConfiguration().getBinaryOperationFactory()
                    .findOperation(TokenType.NOT.name());
            Objects.requireNonNull(operation);
            return new com.kamijoucen.ruler.ast.factor.BinaryOperationNode(
                TokenType.NOT,
                TokenType.NOT.name(),
                manager.parsePrimaryExpression(),
                null,
                operation,
                token.location
            );
        } else {
            throw SyntaxException.withSyntax("不支持的一元运算符: " + token);
        }
    }
}