package ru.akvine.iskra.services.configuration;

import ru.akvine.iskra.repositories.entities.config.TableConfigurationEntity;
import ru.akvine.iskra.services.domain.configuration.TableConfigurationModel;
import ru.akvine.iskra.services.dto.configuration.table.CreateTableConfiguration;
import ru.akvine.iskra.services.dto.configuration.table.UpdateTableConfiguration;

public interface TableConfigurationService {
    TableConfigurationModel create(CreateTableConfiguration action);

    TableConfigurationModel update(UpdateTableConfiguration action);

    TableConfigurationEntity verifyExistsByName(String tableName);
}
