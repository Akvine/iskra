package ru.akvine.iskra.services.configuration;

import ru.akvine.iskra.repositories.entities.config.ColumnConfigurationEntity;
import ru.akvine.iskra.services.domain.configuration.ColumnConfigurationModel;
import ru.akvine.iskra.services.dto.configuration.column.CreateColumnConfiguration;
import ru.akvine.iskra.services.dto.configuration.column.SelectColumnConfiguration;

import java.util.List;

public interface ColumnConfigurationService {
    List<ColumnConfigurationModel> list(String columnUuid);

    ColumnConfigurationModel create(CreateColumnConfiguration action);

    List<ColumnConfigurationModel> select(SelectColumnConfiguration action);

    ColumnConfigurationEntity verifyExistsBy(String columnUuid, String name);
}
