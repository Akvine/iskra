package ru.akvine.iskra.constants;

public final class ServiceTypeConstants {
    private ServiceTypeConstants() throws IllegalAccessException {
        throw new IllegalAccessException("Calling " + ServiceTypeConstants.class.getSimpleName() + " is prohibited!");
    }

    public static final String VISOR = "dbvisor";
    public static final String ISTOCHNIK = "istochnik";
}
