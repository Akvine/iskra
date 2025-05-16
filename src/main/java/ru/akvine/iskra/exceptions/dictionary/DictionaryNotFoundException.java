package ru.akvine.iskra.exceptions.dictionary;

public class DictionaryNotFoundException extends RuntimeException {
    public DictionaryNotFoundException(String message) {
        super(message);
    }
}
