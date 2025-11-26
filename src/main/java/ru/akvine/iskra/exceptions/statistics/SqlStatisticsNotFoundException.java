package ru.akvine.iskra.exceptions.statistics;

public class SqlStatisticsNotFoundException extends RuntimeException {
    public SqlStatisticsNotFoundException(String message) {
        super(message);
    }
}
