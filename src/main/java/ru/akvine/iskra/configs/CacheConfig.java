package ru.akvine.iskra.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.akvine.iskra.enums.CacheType;
import ru.akvine.iskra.services.GeneratorCacheService;
import ru.akvine.iskra.services.impl.cache.InstanceGeneratorCacheServiceImpl;

@Configuration
public class CacheConfig {

    @Value("${cache.type}")
    private String cacheType;

    @Value("${max.generator.cache.size}")
    private int maxGeneratorCacheSize;

    @Bean
    public GeneratorCacheService generatorCacheService() {
        CacheType type = CacheType.from(cacheType);

        if (type == CacheType.INSTANCE) {
            return new InstanceGeneratorCacheServiceImpl(maxGeneratorCacheSize);
        }

        throw new UnsupportedOperationException("Cache type = [" + cacheType + "] is not supported by app!");
    }
}
