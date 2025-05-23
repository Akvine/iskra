package ru.akvine.iskra.services.integration.istochnik;

import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.istochnik.ColumnDto;
import ru.akvine.compozit.commons.istochnik.ConfigDto;
import ru.akvine.compozit.commons.istochnik.GenerateTableRequest;
import ru.akvine.iskra.exceptions.column.configuration.ConfigurationNotSelectedException;
import ru.akvine.iskra.services.domain.configuration.ColumnConfigurationModel;
import ru.akvine.iskra.services.domain.ColumnModel;
import ru.akvine.iskra.services.domain.TableModel;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class IstochnikDtoConverter {
    public GenerateTableRequest convertToGenerateTableRequest(TableModel table) {

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

            columns.add(buildColumnDto(column, config));
        }

        return new GenerateTableRequest()
                .setSize(100)
                .setFileType("csv")
                .setColumns(columns);
    }

    private ColumnDto buildColumnDto(ColumnModel column, ColumnConfigurationModel config) {
        return new ColumnDto()
                .setName(column.getColumnName())
                .setType(config.getType())
                .setGenerationStrategy(config.getGenerationStrategy())
                .setConfig(buildConfigDto(config));
    }

    private ConfigDto buildConfigDto(ColumnConfigurationModel config) {
        ConfigDto configDto = new ConfigDto()
                .setStart(config.getStart())
                .setEnd(config.getEnd())
                .setStep(config.getStep())
                .setRangeType(config.getRangeType())
                .setNotNull(config.isNotNull())
                .setUnique(config.isUnique())
                .setValid(Boolean.TRUE.equals(config.getValid()))
                .setRegexps(new HashSet<>(config.getRegexps()));

        if (config.getDictionary() != null) {
            configDto.setDictionary(config.getDictionary().getValues());
        }

        return configDto;
    }
}
