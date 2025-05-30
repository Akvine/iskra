package ru.akvine.iskra.exceptions;

public class CacheSizeLimitException extends RuntimeException {
    public CacheSizeLimitException(String message) {
        super(message);
    }
}
