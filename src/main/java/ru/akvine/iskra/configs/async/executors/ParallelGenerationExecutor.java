package ru.akvine.iskra.configs.async.executors;

import java.util.concurrent.ThreadPoolExecutor;

public record ParallelGenerationExecutor(ThreadPoolExecutor executor) {
}
