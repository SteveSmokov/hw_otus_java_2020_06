package ru.otus.diplom.exceptions;

public class EntetyException extends RuntimeException {
    public EntetyException(String message) {
        super(message);
    }

    public EntetyException(String message, Throwable cause) {
        super(message, cause);
    }
}
