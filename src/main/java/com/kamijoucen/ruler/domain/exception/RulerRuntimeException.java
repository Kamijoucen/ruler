package com.kamijoucen.ruler.domain.exception;

import com.kamijoucen.ruler.domain.token.TokenLocation;

public class RulerRuntimeException extends RuntimeException {

    private final TokenLocation location;

    public RulerRuntimeException(String message) {
        this(message, null, null);
    }

    public RulerRuntimeException(String message, Throwable cause) {
        this(message, null, cause);
    }

    public RulerRuntimeException(String message, TokenLocation location) {
        this(message, location, null);
    }

    public RulerRuntimeException(String message, TokenLocation location, Throwable cause) {
        super(message, cause);
        this.location = location;
    }

    public TokenLocation getLocation() {
        return location;
    }

    @Override
    public String getMessage() {
        if (location != null) {
            return super.getMessage() + " at " + location.line + ":" + location.column;
        }
        return super.getMessage();
    }
}
