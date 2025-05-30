package ru.akvine.iskra.services.impl.cache;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import ru.akvine.iskra.exceptions.CacheSizeLimitException;
import ru.akvine.iskra.services.GeneratorCacheService;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class InstanceGeneratorCacheServiceImpl implements GeneratorCacheService {
    private static final Map<String, Boolean> STOPPED_PLANS = new ConcurrentHashMap<>();

    private final int size;

    @Override
    public boolean stop(String planUuid) {
        if (StringUtils.isBlank(planUuid)) {
            throw new IllegalArgumentException("Plan uuid can't be blank!");
        }

        if (!STOPPED_PLANS.containsKey(planUuid) && STOPPED_PLANS.size() >= size) {
            String message = String.format(
                    "Can't stop plan with uuid = [%s]. Cache limit = [%s] size is exceeded",
                    planUuid, size
            );
            throw new CacheSizeLimitException(message);
        }

        if (STOPPED_PLANS.containsKey(planUuid) && STOPPED_PLANS.get(planUuid).equals(Boolean.TRUE)) {
            return true;
        }


        STOPPED_PLANS.put(planUuid, true);
        return true;
    }

    @Override
    public boolean isStopped(String planUuid) {
        if (StringUtils.isBlank(planUuid)) {
            throw new IllegalArgumentException("Plan uuid can't be blank!");
        }

        return STOPPED_PLANS.containsKey(planUuid);
    }

    @Override
    public boolean remove(String planUuid) {
        if (StringUtils.isBlank(planUuid)) {
            throw new IllegalArgumentException("Plan uuid can't be blank!");
        }

        if (STOPPED_PLANS.containsKey(planUuid)) {
            STOPPED_PLANS.remove(planUuid);
            return true;
        }

        return false;
    }
}
