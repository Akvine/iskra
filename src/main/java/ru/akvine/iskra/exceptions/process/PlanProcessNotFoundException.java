package ru.akvine.iskra.exceptions.process;

public class PlanProcessNotFoundException extends RuntimeException {
    public PlanProcessNotFoundException(String message) {
        super(message);
    }
}
