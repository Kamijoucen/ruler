package com.kamijoucen.ruler.exception;

import com.kamijoucen.ruler.token.Token;
import com.kamijoucen.ruler.token.TokenLocation;

public class SyntaxException extends RuntimeException {

    private SyntaxException(String s) {
        super(s);
    }

    public static SyntaxException withSyntax(String msg, Token token) {
        return new SyntaxException("syntax error! " + msg);
    }

    public static SyntaxException withLexical(String msg) {
        return new SyntaxException("lexical error! " + msg);
    }

}
