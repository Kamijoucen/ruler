package com.kamijoucen.ruler.parse;

import com.kamijoucen.ruler.token.Token;

public interface Lexical {


    Token getToken();

    Token nextToken();


}
