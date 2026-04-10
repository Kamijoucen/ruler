package com.kamijoucen.ruler.domain.exception;

import com.kamijoucen.ruler.domain.token.TokenLocation;

public class SyntaxException extends RulerRuntimeException {

    public SyntaxException(String message) {
        super(message);
    }

    public SyntaxException(String message, TokenLocation location) {
        super(message, location);
    }
}
