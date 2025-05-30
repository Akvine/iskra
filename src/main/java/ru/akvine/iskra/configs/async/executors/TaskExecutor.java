package ru.akvine.iskra.configs.async.executors;

import java.util.concurrent.ThreadPoolExecutor;

public record TaskExecutor(ThreadPoolExecutor executor) {
    public void execute(Runnable runnable) {

    }
}
