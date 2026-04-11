package com.kamijoucen.ruler.component;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.token.TokenType;

public class TokenStreamImpl implements TokenStream {

    private final Lexical lexical;

    private final List<Token> tokens = new ArrayList<>();

    private int offset = -1;
    private long lastTokenLine = -1;
    private final ArrayDeque<Long> lastTokenLineStack = new ArrayDeque<>();

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
        lastTokenLineStack.push(lastTokenLine);
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
        rollBackToken(1);
    }

    @Override
    public void rollBackToken(int step) {
        for (int i = 0; i < step; i++) {
            if (offset >= 0) {
                offset--;
                if (!lastTokenLineStack.isEmpty()) {
                    lastTokenLine = lastTokenLineStack.pop();
                }
            }
        }
    }

    @Override
    public boolean isNewLine() {
        if (offset < 0 || offset >= tokens.size()) {
            return false;
        }
        return tokens.get(offset).startLine > lastTokenLine;
    }
}
