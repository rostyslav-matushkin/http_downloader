package com.rmatushkin.exception;

public class LimitParseException extends RuntimeException {

    public LimitParseException() {
    }

    public LimitParseException(String message) {
        super(message);
    }

    public LimitParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public LimitParseException(Throwable cause) {
        super(cause);
    }
}
