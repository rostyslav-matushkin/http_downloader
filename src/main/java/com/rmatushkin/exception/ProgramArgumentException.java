package com.rmatushkin.exception;

public class ProgramArgumentException extends RuntimeException {

    public ProgramArgumentException() {
    }

    public ProgramArgumentException(String message) {
        super(message);
    }
}
