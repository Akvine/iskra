package ru.akvine.iskra.exceptions.column.configuration;

public class ConfigurationAlreadyExistsException extends RuntimeException {
    public ConfigurationAlreadyExistsException(String message) {
        super(message);
    }
}
