package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class TokenStreamImpl implements TokenStream {

    private final Lexical lexical;

    private final List<Token> tokens = new ArrayList<>();

    private int offset = -1;
    private long lastTokenLine = -1;

    public TokenStreamImpl(Lexical lexical) {
        this.lexical = lexical;
    }

    public void scan() {
        Token token;
        while ((token = lexical.nextToken()).type != TokenType.EOF) {
            this.tokens.add(token);
        }
        this.tokens.add(lexical.getToken());
    }

    @Override
    public Token token() {
        return this.tokens.get(offset);
    }

    @Override
    public Token nextToken() {
        if (offset >= tokens.size()) {
            return tokens.get(tokens.size() - 1);
        }
        Token prev = offset >= 0 ? tokens.get(offset) : null;
        if (prev != null) {
            lastTokenLine = prev.location.line;
        }
        return tokens.get(++offset);
    }

    @Override
    public Token peek() {
        return peek(1);
    }

    @Override
    public Token peek(int step) {
        if (offset + step >= tokens.size()) {
            return tokens.get(tokens.size() - 1);
        }
        return tokens.get(offset + step);
    }

    @Override
    public Token[] nextToken(int step) {
        return null;
    }

    @Override
    public void rollBackToken() {
        offset--;
    }

    @Override
    public void rollBackToken(int step) {
        offset -= step;
    }

    @Override
    public boolean isNewLine() {
        if (offset < 0 || offset >= tokens.size()) {
            return false;
        }
        return tokens.get(offset).startLine > lastTokenLine;
    }
}
