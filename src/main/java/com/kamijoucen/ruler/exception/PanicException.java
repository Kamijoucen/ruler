package com.kamijoucen.ruler.exception;

public class PanicException extends RuntimeException {

    public PanicException() {
    }

    public PanicException(String message) {
        super(message);
    }

    public PanicException(String message, Throwable cause) {
        super(message, cause);
    }

    public PanicException(Throwable cause) {
        super(cause);
    }

}
