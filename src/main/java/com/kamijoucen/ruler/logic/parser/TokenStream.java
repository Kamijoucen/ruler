package com.kamijoucen.ruler.logic.parser;

import com.kamijoucen.ruler.domain.token.Token;

public interface TokenStream {

    Token token();

    Token nextToken();

    Token peek();

    Token peek(int step);

    Token[] nextToken(int step);

    void rollBackToken();

    void rollBackToken(int step);

    boolean isNewLine();
}
