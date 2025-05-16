package ru.akvine.iskra.exceptions.column;

public class ColumnNotFoundException extends RuntimeException {
    public ColumnNotFoundException(String message) {
        super(message);
    }
}
