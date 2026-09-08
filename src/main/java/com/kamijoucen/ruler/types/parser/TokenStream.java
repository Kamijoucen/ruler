package com.kamijoucen.ruler.types.parser;

import java.util.List;

import com.kamijoucen.ruler.types.token.Token;
import com.kamijoucen.ruler.types.token.TokenType;

public final class TokenStream {

    private final List<Token> tokens;

    private int offset;

    /** The cursor starts at the first token; the list must include EOF. */
    public TokenStream(List<Token> tokens) {
        if (tokens.isEmpty() || tokens.get(tokens.size() - 1).type != TokenType.EOF) {
            throw new IllegalArgumentException("token stream must end with EOF");
        }
        this.tokens = List.copyOf(tokens);
    }

    public Token token() {
        return this.tokens.get(offset);
    }

    public Token nextToken() {
        if (offset + 1 >= tokens.size()) {
            throw new IndexOutOfBoundsException("already at the final token");
        }
        return tokens.get(++offset);
    }

    public Token peek() {
        return peek(1);
    }

    public Token peek(int step) {
        if (offset + step >= tokens.size()) {
            return tokens.get(tokens.size() - 1);
        }
        return tokens.get(offset + step);
    }

    public void rollBackToken() {
        rollBackToken(1);
    }

    public void rollBackToken(int step) {
        if (step > 0) {
            offset = Math.max(0, offset - step);
        }
    }

    public boolean isNewLine() {
        if (offset < 0 || offset >= tokens.size()) {
            return false;
        }
        long previousLine = offset == 0 ? -1 : tokens.get(offset - 1).location.line;
        return tokens.get(offset).startLine > previousLine;
    }
}
