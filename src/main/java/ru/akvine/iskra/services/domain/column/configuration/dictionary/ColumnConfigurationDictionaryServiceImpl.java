package ru.akvine.iskra.services.domain.column.configuration.dictionary;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.repositories.ColumnConfigurationDictionaryRepository;
import ru.akvine.iskra.repositories.entities.DictionaryEntity;
import ru.akvine.iskra.repositories.entities.config.ColumnConfigurationDictionaryEntity;
import ru.akvine.iskra.repositories.entities.config.ColumnConfigurationEntity;
import ru.akvine.iskra.services.domain.column.configuration.ColumnConfigurationModel;

@Service
@RequiredArgsConstructor
public class ColumnConfigurationDictionaryServiceImpl implements ColumnConfigurationDictionaryService {
    private final ColumnConfigurationDictionaryRepository columnConfigurationDictionaryRepository;

    @Override
    public List<ColumnConfigurationDictionaryModel> create(
            ColumnConfigurationEntity columnConfiguration, List<DictionaryEntity> dictionaries) {

        List<ColumnConfigurationDictionaryEntity> columnConfigurationDictionaryEntities = new ArrayList<>();
        for (DictionaryEntity dictionary : dictionaries) {
            ColumnConfigurationDictionaryEntity columnConfigurationDictionaryToCreate =
                    new ColumnConfigurationDictionaryEntity()
                            .setColumnConfiguration(columnConfiguration)
                            .setDictionary(dictionary);
            columnConfigurationDictionaryEntities.add(columnConfigurationDictionaryToCreate);
        }

        return columnConfigurationDictionaryRepository.saveAll(columnConfigurationDictionaryEntities).stream()
                .map(ColumnConfigurationDictionaryModel::new)
                .toList();
    }

    @Override
    public List<ColumnConfigurationDictionaryModel> getAll(ColumnConfigurationModel columnConfiguration) {
        Asserts.isNotNull(columnConfiguration);

        return columnConfigurationDictionaryRepository.findAll(columnConfiguration.getId()).stream()
                .map(ColumnConfigurationDictionaryModel::new)
                .toList();
    }
}
