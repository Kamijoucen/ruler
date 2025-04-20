package com.kamijoucen.ruler.compiler.parser.impl;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.factor.DoubleNode;
import com.kamijoucen.ruler.ast.factor.IntegerNode;
import com.kamijoucen.ruler.compiler.TokenStream;
import com.kamijoucen.ruler.compiler.parser.AtomParser;
import com.kamijoucen.ruler.compiler.parser.AtomParserManager;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;

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
            throw SyntaxException.withSyntax("需要一个数字", token);
        }
    }
}