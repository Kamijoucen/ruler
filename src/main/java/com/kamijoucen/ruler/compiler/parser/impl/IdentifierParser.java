package com.kamijoucen.ruler.compiler.parser.impl;

import com.kamijoucen.ruler.ast.BaseNode;
import com.kamijoucen.ruler.ast.factor.NameNode;
import com.kamijoucen.ruler.ast.factor.OutNameNode;
import com.kamijoucen.ruler.common.MessageType;
import com.kamijoucen.ruler.compiler.TokenStream;
import com.kamijoucen.ruler.compiler.parser.AtomParser;
import com.kamijoucen.ruler.compiler.parser.AtomParserManager;
import com.kamijoucen.ruler.exception.SyntaxException;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;

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
            String message = manager.getConfiguration().getMessageManager()
                    .buildMessage(MessageType.ILLEGAL_IDENTIFIER, token.location, token.name);
            throw new SyntaxException(message);
        }
        manager.getTokenStream().nextToken();
        return nameNode;
    }
}
