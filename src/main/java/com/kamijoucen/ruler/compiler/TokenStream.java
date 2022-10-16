package com.kamijoucen.ruler.compiler;

import com.kamijoucen.ruler.token.Token;

public interface TokenStream {

    Token token();

    Token nextToken();

    Token peek();

    Token peek(int step);

    Token[] nextToken(int step);

    void rollBackToken();

    void rollBackToken(int step);

}
