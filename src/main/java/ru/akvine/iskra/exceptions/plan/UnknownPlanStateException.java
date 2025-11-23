package ru.akvine.iskra.exceptions.plan;

public class UnknownPlanStateException extends RuntimeException {
    public UnknownPlanStateException(String message) {
        super(message);
    }
}
