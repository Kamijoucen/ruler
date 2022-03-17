package com.kamijoucen.ruler.compiler.impl;

import com.kamijoucen.ruler.compiler.Lexical;
import com.kamijoucen.ruler.compiler.TokenStream;
import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenType;

import java.util.ArrayList;
import java.util.List;

public class TokenStreamImpl implements TokenStream {

    private final Lexical lexical;

    private final List<Token> tokens = new ArrayList<Token>();

    private int offset = -1;

    public TokenStreamImpl(Lexical lexical) {
        this.lexical = lexical;
    }

    public void scan() {
        Token token = null;
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
        return tokens.get(++offset);
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
}
