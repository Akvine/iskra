package ru.akvine.iskra.validators;

public interface Validator<T> {
    void validate(T object);
}
