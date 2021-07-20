package com.kamijoucen.ruler.token;

public class Token {

    public final String name;

    public final TokenType type;

    public final TokenLocation location;

    public Token(TokenType type, String name, TokenLocation location) {
        this.type = type;
        this.name = name;
        this.location = location;
    }

    @Override
    public String toString() {
        return "Token{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", location=" + location +
                '}';
    }
}
