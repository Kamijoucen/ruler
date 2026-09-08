package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.types.parser.ParseState;
import com.kamijoucen.ruler.types.parser.TokenStream;

import com.kamijoucen.ruler.types.ast.BaseNode;
import com.kamijoucen.ruler.types.ast.NameNode;
import com.kamijoucen.ruler.types.ast.OutNameNode;

import com.kamijoucen.ruler.types.exception.SyntaxException;
import com.kamijoucen.ruler.types.token.Token;
import com.kamijoucen.ruler.types.token.TokenType;

/**
 * 标识符解析器，处理变量名、函数名等标识符
 */
public class IdentifierParser implements AtomParser {

    @Override
    public boolean support(TokenStream tokenStream) {
        return tokenStream.token().type == TokenType.IDENTIFIER
                || tokenStream.token().type == TokenType.OUT_IDENTIFIER;
    }

    @Override
    public BaseNode parse(ParseState state) {
        Token token = state.tokens.token();
        BaseNode nameNode;
        if (token.type == TokenType.IDENTIFIER) {
            nameNode = new NameNode(token, token.location);
        } else if (token.type == TokenType.OUT_IDENTIFIER) {
            nameNode = new OutNameNode(token, token.location);
        } else {
            throw new SyntaxException("illegal identifier '" + token.name + "'", token.location);
        }
        state.tokens.nextToken();
        return nameNode;
    }
}
