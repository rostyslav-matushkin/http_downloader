package com.rmatushkin.exception;

public class DownloaderException extends RuntimeException {

    public DownloaderException() {
    }

    public DownloaderException(String message) {
        super(message);
    }
}
