package com.kamijoucen.ruler.domain.token;

public class Token {

    public final String name;

    public final TokenType type;

    public final TokenLocation location;

    public final char stringFlag;

    public Token(TokenType type, String name, TokenLocation location) {
        this(type, name, location, '\0');
    }

    public Token(TokenType type, String name, TokenLocation location, char stringFlag) {
        this.type = type;
        this.name = name;
        this.location = location;
        this.stringFlag = stringFlag;
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
