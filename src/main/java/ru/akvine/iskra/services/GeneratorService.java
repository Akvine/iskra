package ru.akvine.iskra.services;

import ru.akvine.iskra.services.dto.GenerateDataAction;

public interface GeneratorService {
    boolean generate(GenerateDataAction action);
}
