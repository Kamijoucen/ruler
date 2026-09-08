package com.kamijoucen.ruler.types.lexer;

import com.kamijoucen.ruler.types.token.Token;

/** Mutable data for one source scan. */
public final class LexerState {
    public final String content;
    public final String fileName;
    public final StringBuilder buffer = new StringBuilder();
    public int offset;
    public int line;
    public int column;
    public LexerMode mode = LexerMode.NONE;
    public Token currentToken;
    public boolean isEnd;
    public char curStringFlag;
    public long tokenStartLine;
    public long tokenStartColumn;

    public LexerState(String content, String fileName) {
        this.content = content;
        this.fileName = fileName;
    }
}
