package ru.akvine.iskra.services.domain.column.configuration.dictionary;

import java.util.List;
import ru.akvine.iskra.repositories.entities.DictionaryEntity;
import ru.akvine.iskra.repositories.entities.config.ColumnConfigurationEntity;
import ru.akvine.iskra.services.domain.column.configuration.ColumnConfigurationModel;

public interface ColumnConfigurationDictionaryService {
    List<ColumnConfigurationDictionaryModel> create(
            ColumnConfigurationEntity columnConfiguration, List<DictionaryEntity> dictionaries);

    List<ColumnConfigurationDictionaryModel> getAll(ColumnConfigurationModel columnConfiguration);
}
