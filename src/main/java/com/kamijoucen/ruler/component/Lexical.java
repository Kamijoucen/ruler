package com.kamijoucen.ruler.component;

import com.kamijoucen.ruler.domain.token.Token;

public interface Lexical {

    Token getToken();

    Token nextToken();

    void scanComment();

    void scanSymbol();

    void scanString();

    void scanStringBlock();

    void scanNumber();

    void scanOutIdentifier();

    void scanIdentifier();
}
