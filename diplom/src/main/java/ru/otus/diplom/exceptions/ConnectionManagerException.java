package ru.otus.diplom.exceptions;

public class ConnectionManagerException extends RuntimeException {
    public ConnectionManagerException(Exception e) {
        super(e);
    }
}
