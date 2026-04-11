package com.kamijoucen.ruler.domain.token;

public class Token {

    public final String name;

    public final TokenType type;

    public final TokenLocation location;

    public final char stringFlag;

    public final long startLine;

    public final long startColumn;

    public Token(TokenType type, String name, TokenLocation location) {
        this(type, name, location, '\0', location.line, location.column);
    }

    public Token(TokenType type, String name, TokenLocation location, char stringFlag) {
        this(type, name, location, stringFlag, location.line, location.column);
    }

    public Token(TokenType type, String name, TokenLocation location, char stringFlag, long startLine, long startColumn) {
        this.type = type;
        this.name = name;
        this.location = location;
        this.stringFlag = stringFlag;
        this.startLine = startLine;
        this.startColumn = startColumn;
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
