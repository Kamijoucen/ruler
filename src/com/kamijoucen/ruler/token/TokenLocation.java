package com.kamijoucen.ruler.token;

public class TokenLocation {

    public final long line;
    public final long column;

    public TokenLocation(long line, long column) {
        this.line = line;
        this.column = column;
    }

}
