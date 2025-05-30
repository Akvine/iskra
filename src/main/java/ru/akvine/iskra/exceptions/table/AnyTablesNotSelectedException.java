package ru.akvine.iskra.exceptions.table;

public class AnyTablesNotSelectedException extends RuntimeException {
    public AnyTablesNotSelectedException(String message) {
        super(message);
    }
}
