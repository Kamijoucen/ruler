package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.factor.DoubleNode;
import com.kamijoucen.ruler.domain.ast.factor.IntegerNode;
import com.kamijoucen.ruler.component.TokenStream;
import com.kamijoucen.ruler.component.AtomParser;
import com.kamijoucen.ruler.component.AtomParserManager;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.token.TokenType;

/**
 * 数字字面量解析器
 */
public class NumberParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.INTEGER ||
               tokenStream.token().type == TokenType.DOUBLE;
    }

    @Override
    public BaseNode parse(AtomParserManager manager) {
        TokenStream tokenStream = manager.getTokenStream();
        Token token = tokenStream.token();
        tokenStream.nextToken();

        if (token.type == TokenType.INTEGER) {
            return new IntegerNode(Long.parseLong(token.name), token.location);
        } else if (token.type == TokenType.DOUBLE) {
            return new DoubleNode(Double.parseDouble(token.name), token.location);
        } else {
            throw new SyntaxException("expected a number\t token=" + token);
        }
    }
}