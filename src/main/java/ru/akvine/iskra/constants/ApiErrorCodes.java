package ru.akvine.iskra.constants;

public final class ApiErrorCodes {
    private ApiErrorCodes() throws IllegalAccessException {
        throw new IllegalAccessException("Calling " + ApiErrorCodes.class.getSimpleName() + " is prohibited!");
    }

    public static final String GENERAL_ERROR = "general.error";
    public static final String NO_SESSION = "no.session.error";

    public static final String FIELD_INVALID_FORMAT = "field.invalid.format.error";

    public interface Validation {
        String ELEMENTS_MAX_COUNT_ERROR = "elements.max.count.error";
        String FILE_EXTENSION_NOT_SUPPORTED = "file.extension.not.supported.error";
        String FILTERS_MAX_COUNT_ERROR = "filters.max.count.error";
        String PASSWORD_INVALID_ERROR = "password.invalid.error";
        String EMAIL_INVALID_ERROR = "email.invalid.error";
    }

    public interface Plan {
        String PLAN_NOT_FOUND_ERROR = "plan.notFound.error";
    }

    public interface Process {
        String PROCESS_NOT_FOUND_ERROR = "tableProcess.notFound.error";
        String PLAN_PROCESS_NOT_FOUND_ERROR = "planProcess.notFound.error";
    }

    public interface Connection {
        String CONNECTION_NOT_FOUND_ERROR = "connection.notFound.error";
    }

    public interface Configuration {
        String TABLE_CONFIGURATION_ERROR = "table.configuration.error";
    }
}
