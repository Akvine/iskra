package ru.akvine.iskra.services.domain.column.configuration;

import org.springframework.transaction.annotation.Transactional;
import ru.akvine.iskra.repositories.entities.config.ColumnConfigurationEntity;
import ru.akvine.iskra.services.domain.column.configuration.dto.CreateColumnConfiguration;
import ru.akvine.iskra.services.domain.column.configuration.dto.SelectColumnConfiguration;

import java.util.List;

public interface ColumnConfigurationService {
    @Transactional
    List<ColumnConfigurationModel> list(String columnUuid);

    ColumnConfigurationModel create(CreateColumnConfiguration action);

    List<ColumnConfigurationModel> select(SelectColumnConfiguration action);

    ColumnConfigurationEntity getSelected(String planUuid, String tableName, String columnName);

    ColumnConfigurationEntity verifyExistsBy(String columnUuid, String name);

    @Transactional
    List<ColumnConfigurationModel> generateForExternalRelations(String planUuid);
}
