package ru.akvine.iskra.exceptions.plan;

public class PlanAlreadyStartedException extends RuntimeException {
    public PlanAlreadyStartedException(String message) {
        super(message);
    }
}
