package ru.akvine.iskra.services;

public interface PlanActionService {

    // TODO: по Clean Code передача boolean-параметров - плохая тактика. Придумать что-то по лучше
    String start(String planUuid, boolean resume);

    boolean stop(String planUuid);

    String resume(String planUuid);
}
