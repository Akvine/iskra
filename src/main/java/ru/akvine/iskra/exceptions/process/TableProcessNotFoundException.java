package ru.akvine.iskra.exceptions.process;

public class TableProcessNotFoundException extends RuntimeException {
    public TableProcessNotFoundException(String message) {
        super(message);
    }
}
