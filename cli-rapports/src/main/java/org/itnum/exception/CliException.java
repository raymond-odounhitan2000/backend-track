package org.itnum.exception;

public class CliException extends RuntimeException {
    public CliException(String message) {
        super(message);
    }

    public CliException(String message, Throwable reason) {
        super(message, reason);
    }
}