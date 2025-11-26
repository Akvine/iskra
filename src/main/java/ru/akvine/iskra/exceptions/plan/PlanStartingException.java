package ru.akvine.iskra.exceptions.plan;

public class PlanStartingException extends RuntimeException {
    public PlanStartingException(String message, Exception exception) {
        super(message, exception);
    }
}
