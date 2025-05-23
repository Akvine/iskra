package ru.akvine.iskra.rest.converter.configuration;

import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.rest.dto.configuration.table.CreateTableConfigurationRequest;
import ru.akvine.iskra.rest.dto.configuration.table.ListTableConfigurationsResponse;
import ru.akvine.iskra.rest.dto.configuration.table.TableConfigurationDto;
import ru.akvine.iskra.services.domain.configuration.TableConfigurationModel;
import ru.akvine.iskra.services.dto.configuration.table.CreateTableConfiguration;

import java.util.List;

@Component
public class TableConfigurationConverter {
    public CreateTableConfiguration convertToCreateTableConfiguration(CreateTableConfigurationRequest request) {
        Asserts.isNotNull(request);
        return new CreateTableConfiguration()
                .setTableName(request.getTableName())
                .setBatchSize(request.getBatchSize())
                .setRowsCount(request.getRowsCount())
                .setName(request.getName());
    }

    public ListTableConfigurationsResponse convertToListTableConfigurationsResponse(List<TableConfigurationModel> configurations) {
        return new ListTableConfigurationsResponse()
                .setConfigurations(configurations.stream().map(this::buildTableConfigurationDto).toList());
    }

    private TableConfigurationDto buildTableConfigurationDto(TableConfigurationModel config) {
        return new TableConfigurationDto()
                .setName(config.getName())
                .setBatchSize(config.getBatchSize())
                .setName(config.getName());
    }
}
