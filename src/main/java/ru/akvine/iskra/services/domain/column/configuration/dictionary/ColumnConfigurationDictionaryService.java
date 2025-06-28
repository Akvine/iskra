package ru.akvine.iskra.services.domain.column.configuration.dictionary;

import ru.akvine.iskra.repositories.entities.DictionaryEntity;
import ru.akvine.iskra.repositories.entities.config.ColumnConfigurationEntity;
import ru.akvine.iskra.services.domain.column.configuration.ColumnConfigurationModel;

import java.util.List;

public interface ColumnConfigurationDictionaryService {
    List<ColumnConfigurationDictionaryModel> create(ColumnConfigurationEntity columnConfiguration, List<DictionaryEntity> dictionaries);

    List<ColumnConfigurationDictionaryModel> getAll(ColumnConfigurationModel columnConfiguration);
}
