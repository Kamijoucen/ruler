package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.types.parser.ParseState;
import com.kamijoucen.ruler.types.parser.TokenStream;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.BinaryOperationNode;
import com.kamijoucen.ruler.types.ast.UnaryOperationNode;

import com.kamijoucen.ruler.types.exception.SyntaxException;
import com.kamijoucen.ruler.types.token.Token;
import com.kamijoucen.ruler.types.token.TokenType;


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
    public BaseNode parse(ParseState state) {
        TokenStream tokenStream = state.tokens;
        Token token = tokenStream.token();
        tokenStream.nextToken();

        if (token.type == TokenType.ADD || token.type == TokenType.SUB) {
            return new UnaryOperationNode(
                token.type,
                Parser.parsePrimaryExpression(state),
                token.location
            );
        } else if (token.type == TokenType.NOT) {
            return new BinaryOperationNode(
                TokenType.NOT,
                TokenType.NOT.name(),
                Parser.parsePrimaryExpression(state),
                null,
                token.location
            );
        } else {
            throw new SyntaxException("unsupported unary operator: " + token);
        }
    }
}
