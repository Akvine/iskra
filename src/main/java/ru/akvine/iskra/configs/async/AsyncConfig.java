package ru.akvine.iskra.configs.async;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import ru.akvine.iskra.configs.async.executors.TaskExecutor;
import ru.akvine.iskra.configs.properties.ParallelExecutorProperties;
import ru.akvine.iskra.services.GeneratorCacheService;
import ru.akvine.iskra.services.GeneratorService;
import ru.akvine.iskra.services.domain.table.process.TableProcessService;
import ru.akvine.iskra.services.impl.RestGeneratorServiceImpl;
import ru.akvine.iskra.services.integration.istochnik.IstochnikService;
import ru.akvine.iskra.services.integration.visor.VisorService;
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
    public TaskExecutor parallelGenerationExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                parallelExecutorProperties.getThreadsCount(),
                parallelExecutorProperties.getThreadsCount(),
                0,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(parallelExecutorProperties.getQueueCapacity()),
                ThreadsUtils.newThreadFactory(PARALLEL_EXECUTOR_BASE_NAME));
        return new TaskExecutor(executor);
    }

    @Bean
    public GeneratorService generatorService(final VisorService visorService,
                                             final IstochnikService istochnikService,
                                             final TableProcessService tableProcessService,
                                             final GeneratorCacheService generatorCacheService) {
        return new RestGeneratorServiceImpl(
                visorService,
                istochnikService,
                tableProcessService,
                generatorCacheService);
    }
}
