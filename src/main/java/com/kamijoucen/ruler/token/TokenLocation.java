package com.kamijoucen.ruler.token;

public class TokenLocation {

    public final long line;
    public final long column;
    public final String fileName;

    public TokenLocation(long line, long column, String fileName) {
        this.line = line;
        this.column = column;
        this.fileName = fileName;
    }

    @Override
    public String toString() {
        return "TokenLocation{" +
                "line=" + line +
                ", column=" + column +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
