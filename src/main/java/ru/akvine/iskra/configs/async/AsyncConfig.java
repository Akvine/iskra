package ru.akvine.iskra.configs.async;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.akvine.iskra.configs.async.executors.ParallelGenerationExecutor;
import ru.akvine.iskra.configs.properties.ParallelExecutorProperties;
import ru.akvine.iskra.utils.ThreadsUtils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAsync
@RequiredArgsConstructor
public class AsyncConfig {
    private final ParallelExecutorProperties parallelExecutorProperties;

    private static final String PARALLEL_EXECUTOR_BASE_NAME = "parallel-generation-data-executor";

    // TODO: сделать бин необязательным, если настройка выключена parallel.execution.enabled=false
    @Bean
    public ParallelGenerationExecutor parallelGenerationExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                parallelExecutorProperties.getThreadsCount(),
                parallelExecutorProperties.getThreadsCount(),
                0,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(parallelExecutorProperties.getQueueCapacity()),
                ThreadsUtils.newThreadFactory(PARALLEL_EXECUTOR_BASE_NAME));
        return new ParallelGenerationExecutor(executor);
    }
}
