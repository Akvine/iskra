package ru.akvine.iskra.providers;

import ru.akvine.iskra.enums.GenerationStrategy;
import ru.akvine.iskra.services.GenerationStrategyHandler;

import java.util.Map;

public record GenerationStrategyHandlersProvider(Map<GenerationStrategy, GenerationStrategyHandler> handlers) {
    public GenerationStrategyHandler getByStrategy(GenerationStrategy strategy) {
        if (handlers.containsKey(strategy)) {
            return handlers.get(strategy);
        }

        throw new UnsupportedOperationException(
                "Generation strategy handler with strategy = [" + strategy + "] is not supported by app!"
        );
    }
}
