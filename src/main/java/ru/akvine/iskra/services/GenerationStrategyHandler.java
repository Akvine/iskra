package ru.akvine.iskra.services;

import ru.akvine.iskra.enums.GenerationStrategy;
import ru.akvine.iskra.services.dto.GenerateData;

public interface GenerationStrategyHandler {
    boolean generate(GenerateData action);

    GenerationStrategy getStrategy();
}
