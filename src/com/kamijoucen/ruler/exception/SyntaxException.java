package com.kamijoucen.ruler.exception;

import com.kamijoucen.ruler.token.Token;

public class SyntaxException extends RuntimeException {

    private SyntaxException(String s) {
        super(s);
    }

    public static SyntaxException withSyntax(String msg) {
        return new SyntaxException("syntax error! " + msg);
    }

    public static SyntaxException withSyntax(String msg, Token token) {
        return new SyntaxException("syntax error! " + msg + "\t token=" + token);
    }

    public static SyntaxException withLexical(String msg) {
        return new SyntaxException("lexical error! " + msg);
    }

}
