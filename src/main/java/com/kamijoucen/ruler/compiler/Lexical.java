package com.kamijoucen.ruler.compiler;

import com.kamijoucen.ruler.token.Token;

public interface Lexical {

    Token getToken();

    Token nextToken();

    void scanComment();

    void scanSymbol();

    void scanString();

    void scanNumber();

    void scanOutIdentifier();

    void scanIdentifier();
}
