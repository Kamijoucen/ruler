package com.kamijoucen.ruler.parse;

import com.kamijoucen.ruler.token.Token;

public interface TokenStream {

    Token nextToken();

    Token[] nextToken(int step);

    void rollBackToken();

    void rollBackToken(int step);

}
