package ru.akvine.iskra.services.integration.istochnik;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.istochnik.ColumnDto;
import ru.akvine.compozit.commons.istochnik.ConfigDto;
import ru.akvine.compozit.commons.istochnik.GenerateTableRequest;
import ru.akvine.iskra.exceptions.column.configuration.ConfigurationNotSelectedException;
import ru.akvine.iskra.services.domain.column.ColumnModel;
import ru.akvine.iskra.services.domain.column.configuration.ColumnConfigurationModel;
import ru.akvine.iskra.services.domain.table.TableModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class IstochnikDtoMapper {
    public GenerateTableRequest convertToGenerateTableRequest(int processedRowsCount,
                                                              TableModel table) {

        int batchSize = table.getConfiguration().getBatchSize();
        List<ColumnDto> columns = new ArrayList<>();
        for (ColumnModel column : table.getColumns()) {
            ColumnConfigurationModel config = column.getConfigurations().stream()
                    .filter(ColumnConfigurationModel::isSelected)
                    .findFirst()
                    .orElseThrow(() -> {
                        String message = String.format(
                                "Has no selected or created config for column = [%s], table = [%s]",
                                column.getColumnName(),
                                column.getTableName()
                        );
                        return new ConfigurationNotSelectedException(message);
                    });

            columns.add(buildColumnDto(column, config, processedRowsCount, batchSize));
        }

        return new GenerateTableRequest()
                .setSize(table.getConfiguration().getBatchSize())
                .setFileType("csv")
                .setColumns(columns);
    }

    private ColumnDto buildColumnDto(ColumnModel column,
                                     ColumnConfigurationModel config,
                                     int processedRowsCount,
                                     int batchSize) {
        return new ColumnDto()
                .setName(column.getColumnName())
                .setType(config.getType())
                .setGenerationStrategy(config.getGenerationStrategy())
                .setConvertToString(config.isConvertToString())
                .setConverters(config.getConverters())
                .setPostConverters(config.getPostConverters())
                .setConfig(buildConfigDto(batchSize, processedRowsCount, config));
    }

    private ConfigDto buildConfigDto(int batchSize, int processedRowsCount, ColumnConfigurationModel config) {
        ConfigDto configDto = new ConfigDto()
                .setStart(config.getStart())
                .setStep(config.getStep())
                .setEnd(config.getEnd())
                .setRangeType(config.getRangeType())
                .setNotNull(config.isNotNull())
                .setUnique(config.isUnique())
                .setValid(Boolean.TRUE.equals(config.getValid()))
                .setRegexps(new HashSet<>(config.getRegexps()));

        if (!config.isRepeatable()) {
            configDto.setStart(String.valueOf(processedRowsCount + Integer.parseInt(config.getStart())));
            configDto.setEnd(String.valueOf(processedRowsCount + batchSize + Integer.parseInt(config.getStart())));
        }

        if (CollectionUtils.isNotEmpty(config.getDictionaries())) {
            Set<Set<String>> dictionaries = new HashSet<>();
            config.getDictionaries().forEach(dictionary -> dictionaries.add(dictionary.getDictionaryValues()));
            configDto.setDictionaries(dictionaries);
        }

        return configDto;
    }
}
