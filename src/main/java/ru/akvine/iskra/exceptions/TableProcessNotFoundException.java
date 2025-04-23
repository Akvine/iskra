package ru.akvine.iskra.exceptions;

public class TableProcessNotFoundException extends RuntimeException {
    public TableProcessNotFoundException(String message) {
        super(message);
    }
}
