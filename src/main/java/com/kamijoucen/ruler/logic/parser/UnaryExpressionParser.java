package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.factor.BinaryOperationNode;
import com.kamijoucen.ruler.domain.ast.factor.UnaryOperationNode;
import com.kamijoucen.ruler.component.TokenStream;
import com.kamijoucen.ruler.component.AtomParser;
import com.kamijoucen.ruler.component.AtomParserManager;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.logic.operation.BinaryOperation;
import com.kamijoucen.ruler.logic.operation.UnaryAddOperation;
import com.kamijoucen.ruler.logic.operation.UnarySubOperation;
import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.token.TokenType;

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
            return new BinaryOperationNode(
                TokenType.NOT,
                TokenType.NOT.name(),
                manager.parsePrimaryExpression(),
                null,
                operation,
                token.location
            );
        } else {
            throw new SyntaxException("unsupported unary operator: " + token);
        }
    }
}