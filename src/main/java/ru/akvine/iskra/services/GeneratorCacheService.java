package ru.akvine.iskra.services;

public interface GeneratorCacheService {
    boolean stop(String planUuid);

    boolean isStopped(String planUuid);

    boolean remove(String planUuid);
}
