package com.rmatushkin.exception;

public class ProgramArgumentException extends RuntimeException {

    public ProgramArgumentException() {
    }

    public ProgramArgumentException(String message) {
        super(message);
    }

    public ProgramArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProgramArgumentException(Throwable cause) {
        super(cause);
    }
}
