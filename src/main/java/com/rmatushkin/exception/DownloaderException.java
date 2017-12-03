package com.rmatushkin.exception;

public class DownloaderException extends RuntimeException {

    public DownloaderException() {
    }

    public DownloaderException(String message) {
        super(message);
    }

    public DownloaderException(String message, Throwable cause) {
        super(message, cause);
    }

    public DownloaderException(Throwable cause) {
        super(cause);
    }
}
