package ru.akvine.iskra.exceptions.regex;

public class RegexAlreadyExistsException extends RuntimeException {
    public RegexAlreadyExistsException(String message) {
        super(message);
    }
}
