package com.rmatushkin.exception;

public class HttpClientException extends RuntimeException {

    public HttpClientException() {
    }

    public HttpClientException(String message) {
        super(message);
    }
}
