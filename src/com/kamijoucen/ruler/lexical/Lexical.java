package com.kamijoucen.ruler.lexical;

import com.kamijoucen.ruler.token.Token;

public interface Lexical {


    Token getToken();

    Token nextToken();


}
