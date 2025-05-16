package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.exceptions.column.configuration.ConfigurationMaxCountException;
import ru.akvine.iskra.repositories.ColumnConfigurationRepository;
import ru.akvine.iskra.repositories.entities.ColumnConfigurationEntity;
import ru.akvine.iskra.repositories.entities.ColumnEntity;
import ru.akvine.iskra.repositories.entities.DictionaryEntity;
import ru.akvine.iskra.services.ColumnConfigurationService;
import ru.akvine.iskra.services.ColumnService;
import ru.akvine.iskra.services.DictionaryService;
import ru.akvine.iskra.services.domain.ColumnConfigurationModel;
import ru.akvine.iskra.services.dto.configuration.CreateColumnConfiguration;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ColumnConfigurationServiceImpl implements ColumnConfigurationService {
    private final ColumnConfigurationRepository columnConfigurationRepository;

    @Value("${max.configs.per.column}")
    private int maxConfigsPerColumn;

    private final ColumnService columnService;
    private final DictionaryService dictionaryService;

    @Override
    public List<ColumnConfigurationModel> list(String columnUuid) {
        Asserts.isNotNull(columnUuid);
        return columnConfigurationRepository
                .findAll(columnUuid).stream()
                .map(ColumnConfigurationModel::new)
                .toList();
    }

    @Override
    public ColumnConfigurationModel create(CreateColumnConfiguration action) {
        Asserts.isNotNull(action);

        ColumnEntity column = columnService.verifyExists(action.getColumnUuid());

        if (columnConfigurationRepository.count(column.getUuid()) == maxConfigsPerColumn) {
            String message = "Max configurations count [" + maxConfigsPerColumn + "] was exceeded! Remove unnecessary configurations";
            throw new ConfigurationMaxCountException(message);
        }

        ColumnConfigurationEntity columnConfigurationToCreate = new ColumnConfigurationEntity()
                .setSelected(action.isSelected())
                .setName(action.getName())
                .setType(action.getType())
                .setGenerationStrategy(action.getGenerationStrategy())
                .setUnique(action.isUnique())
                .setNotNull(action.isNotNull())
                .setRangeType(action.getRangeType())
                .setStart(action.getStart())
                .setEnd(action.getEnd())
                .setStep(action.getStep())
                .setValid(action.getValid())
                .setRegexps(String.join(";", action.getRegexps()))
                .setColumn(column);

        if (StringUtils.isNotBlank(action.getDictionaryName())) {
            DictionaryEntity dictionary = dictionaryService.verifyExists(action.getDictionaryName());
            columnConfigurationToCreate.setDictionary(dictionary);
        }

        return new ColumnConfigurationModel(columnConfigurationRepository.save(columnConfigurationToCreate));
    }
}
