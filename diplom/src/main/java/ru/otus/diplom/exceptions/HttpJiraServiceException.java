package ru.otus.diplom.exceptions;

public class HttpJiraServiceException extends RuntimeException {
    public HttpJiraServiceException(String message) {
        super(message);
    }

    public HttpJiraServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
