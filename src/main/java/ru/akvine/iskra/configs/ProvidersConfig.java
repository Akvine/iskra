package ru.akvine.iskra.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.akvine.iskra.enums.GenerationStrategy;
import ru.akvine.iskra.providers.GenerationStrategyHandlersProvider;
import ru.akvine.iskra.services.GenerationStrategyHandler;

import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Configuration
public class ProvidersConfig {

    @Bean
    public GenerationStrategyHandlersProvider generationStrategyHandlersProvider(List<GenerationStrategyHandler> handlerList) {
        Map<GenerationStrategy, GenerationStrategyHandler> handlers = handlerList
                .stream()
                .collect(toMap(GenerationStrategyHandler::getStrategy, identity()));
        return new GenerationStrategyHandlersProvider(handlers);
    }
}
