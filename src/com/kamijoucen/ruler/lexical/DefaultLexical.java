package com.kamijoucen.ruler.lexical;

import com.kamijoucen.ruler.token.Token;

public class DefaultLexical implements Lexical {

    private String content;

    private Token currentToken;

    public DefaultLexical(String content) {
        this.content = content;
    }


    @Override
    public Token getToken() {
        return currentToken;
    }

    @Override
    public Token nextToken() {
        return null;
    }
}
