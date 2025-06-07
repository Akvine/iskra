package ru.akvine.iskra.services.impl.strategy;

import lombok.RequiredArgsConstructor;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.services.GenerationStrategyHandler;
import ru.akvine.iskra.services.MatrixService;
import ru.akvine.iskra.services.dto.GenerateData;

@RequiredArgsConstructor
public abstract class AbstractGenerationStrategyHandler implements GenerationStrategyHandler {
    private final MatrixService matrixService;

    @Override
    public boolean generate(GenerateData action) {
        Asserts.isNotNull(action);
        return true;
    }
}
