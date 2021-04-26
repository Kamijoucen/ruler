package com.kamijoucen.ruler.exception;

public class SyntaxException extends RuntimeException {

    private SyntaxException(String s) {
        super(s);
    }

    public static SyntaxException withSyntax(String msg) {
        return new SyntaxException("syntax error! " + msg);
    }

    public static SyntaxException withLexical(String msg) {
        return new SyntaxException("lexical error! " + msg);
    }

}
