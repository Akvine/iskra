package ru.akvine.iskra.configs.properties;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "parallel")
@Getter
@Setter
@Validated
public class ParallelGenerationProperties {
    private int threadsCorePoolSize;
    private int threadsMaxPoolSize;
    private long threadsKeepAliveSeconds;
    private int queueCapacity;
}
