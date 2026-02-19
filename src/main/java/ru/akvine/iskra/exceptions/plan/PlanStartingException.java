package ru.akvine.iskra.exceptions.plan;

public class PlanStartingException extends RuntimeException {
    public PlanStartingException(String message, Throwable exception) {
        super(message, exception);
    }
}
