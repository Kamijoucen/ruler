package com.kamijoucen.ruler.token;

public class Token {

    public final TokenType type;

    public final TokenLocation location;

    public Token(TokenType type, TokenLocation location) {
        this.type = type;
        this.location = location;
    }

}
