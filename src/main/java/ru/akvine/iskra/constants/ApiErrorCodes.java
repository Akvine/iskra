package ru.akvine.iskra.constants;

public final class ApiErrorCodes {
    private ApiErrorCodes() throws IllegalAccessException {
        throw new IllegalAccessException("Calling " + ApiErrorCodes.class.getSimpleName() + " is prohibited!");
    }

    public static final String GENERAL_ERROR = "general.error";

    public interface Plan {
        String PLAN_NOT_FOUND_ERROR = "plan.notFound.error";
    }

    public interface Process {
        String PROCESS_NOT_FOUND_ERROR = "tableProcess.notFound.error";
    }

    public interface Connection {
        String CONNECTION_NOT_FOUND_ERROR = "connection.notFound.error";
    }
}
