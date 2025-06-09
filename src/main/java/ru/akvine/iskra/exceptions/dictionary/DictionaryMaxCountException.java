package ru.akvine.iskra.exceptions.dictionary;

public class DictionaryMaxCountException extends RuntimeException {
    public DictionaryMaxCountException(String message) {
        super(message);
    }
}
