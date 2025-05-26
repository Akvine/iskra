package ru.akvine.iskra.configs.async;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.akvine.iskra.configs.async.executors.ParallelGenerationExecutor;
import ru.akvine.iskra.configs.properties.ParallelGenerationProperties;
import ru.akvine.iskra.utils.ThreadsUtils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAsync
@RequiredArgsConstructor
public class AsyncConfig {
    private final ParallelGenerationProperties parallelGenerationProperties;

    // TODO: сделать через @ConfigurationProperties
    @Value("${parallel.executor.threads.core.poolSize}")
    private int corePoolSize;
    @Value("${parallel.executor.threads.max.poolSize}")
    private int maxPoolSize;
    @Value("${parallel.executor.threads.keepAlive.seconds}")
    private long keepAliveSeconds;

    private static final String PARALLEL_EXECUTOR_BASE_NAME = "parallel-generation-data-executor";

    @Bean
    public ParallelGenerationExecutor parallelGenerationExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveSeconds,
                TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                ThreadsUtils.newThreadFactory(PARALLEL_EXECUTOR_BASE_NAME));
        return new ParallelGenerationExecutor(executor);
    }
}
