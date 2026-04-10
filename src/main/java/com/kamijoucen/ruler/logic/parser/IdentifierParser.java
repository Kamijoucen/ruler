package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.domain.ast.BaseNode;
import com.kamijoucen.ruler.domain.ast.factor.NameNode;
import com.kamijoucen.ruler.domain.ast.factor.OutNameNode;
import com.kamijoucen.ruler.component.TokenStream;
import com.kamijoucen.ruler.component.AtomParser;
import com.kamijoucen.ruler.component.AtomParserManager;
import com.kamijoucen.ruler.domain.exception.SyntaxException;
import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.token.TokenType;

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
    public BaseNode parse(AtomParserManager manager) {
        Token token = manager.getTokenStream().token();
        BaseNode nameNode;
        if (token.type == TokenType.IDENTIFIER) {
            nameNode = new NameNode(token, token.location);
        } else if (token.type == TokenType.OUT_IDENTIFIER) {
            nameNode = new OutNameNode(token, token.location);
        } else {
            throw new SyntaxException("illegal identifier '" + token.name + "'", token.location);
        }
        manager.getTokenStream().nextToken();
        return nameNode;
    }
}
