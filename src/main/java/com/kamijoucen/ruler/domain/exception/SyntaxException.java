package com.kamijoucen.ruler.domain.exception;

import com.kamijoucen.ruler.domain.token.Token;
import com.kamijoucen.ruler.domain.token.TokenLocation;

public class SyntaxException extends RuntimeException {

    public SyntaxException(String s) {
        super(s);
    }

    @Deprecated
    public static SyntaxException withSyntax(String msg) {
        return new SyntaxException("syntax error! " + msg);
    }

    @Deprecated
    public static SyntaxException withSyntax(String msg, Token token) {
        return new SyntaxException("syntax error! " + msg + "\t token=" + token);
    }

    @Deprecated
    public static SyntaxException withSyntax(String msg, TokenLocation location) {
        return new SyntaxException("syntax error! " + msg + "\t location=" + location);
    }

}
