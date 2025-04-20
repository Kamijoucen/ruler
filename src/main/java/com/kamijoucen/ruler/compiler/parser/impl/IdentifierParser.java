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
        return tokenStream.token().type == TokenType.IDENTIFIER ||
               tokenStream.token().type == TokenType.OUT_IDENTIFIER;
    }

    @Override
    public BaseNode parse(AtomParserManager manager) {
        return manager.parseIdentifier();
    }
}