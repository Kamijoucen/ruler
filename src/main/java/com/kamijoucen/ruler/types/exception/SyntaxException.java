package com.kamijoucen.ruler.types.exception;

import com.kamijoucen.ruler.types.token.TokenLocation;

public class SyntaxException extends RulerRuntimeException {

    public SyntaxException(String message) {
        super(message);
    }

    public SyntaxException(String message, TokenLocation location) {
        super(message, location);
    }
}
