package com.example.exception;

public class OPhimException extends RuntimeException {
    private final int status;

    public OPhimException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int status() {
        return status;
    }
}
