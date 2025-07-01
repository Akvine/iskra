package ru.akvine.iskra.rest.mappers.configuration;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.enums.DeleteMode;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.components.SecurityManager;
import ru.akvine.iskra.rest.dto.configuration.table.CreateTableConfigurationRequest;
import ru.akvine.iskra.rest.dto.configuration.table.ListTableConfigurationsResponse;
import ru.akvine.iskra.rest.dto.configuration.table.TableConfigurationDto;
import ru.akvine.iskra.rest.dto.configuration.table.UpdateTableConfigurationRequest;
import ru.akvine.iskra.services.domain.table.configuration.TableConfigurationModel;
import ru.akvine.iskra.services.domain.table.configuration.dto.CreateTableConfiguration;
import ru.akvine.iskra.services.domain.table.configuration.dto.UpdateTableConfiguration;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TableConfigurationMapper {
    private final SecurityManager securityManager;

    public CreateTableConfiguration convertToCreateTableConfiguration(CreateTableConfigurationRequest request) {
        Asserts.isNotNull(request);
        return new CreateTableConfiguration()
                .setUserUuid(securityManager.getCurrentUser().getUuid())
                .setPlanUuid(request.getPlanUuid())
                .setTableName(request.getTableName())
                .setBatchSize(request.getBatchSize())
                .setRowsCount(request.getRowsCount())
                .setName(request.getName())
                .setDeleteDataBeforeStart(request.getDeleteDataBeforeStart())
                .setDeleteMode(StringUtils.isBlank(request.getDeleteMode()) ? null : DeleteMode.from(request.getDeleteMode()))
                .setGenerateClearScript(request.getGenerateClearScript());
    }

    public UpdateTableConfiguration convertToUpdateTableConfiguration(UpdateTableConfigurationRequest request) {
        Asserts.isNotNull(request);
        return new UpdateTableConfiguration()
                .setUserUuid(securityManager.getCurrentUser().getUuid())
                .setPlanUuid(request.getPlanUuid())
                .setTableName(request.getTableName())
                .setBatchSize(request.getBatchSize())
                .setRowsCount(request.getRowsCount())
                .setName(request.getName())
                .setDeleteDataBeforeStart(request.getDeleteDataBeforeStart())
                .setDeleteMode(StringUtils.isBlank(request.getDeleteMode()) ? null : DeleteMode.from(request.getDeleteMode()))
                .setGenerateClearScript(request.getGenerateClearScript());
    }

    public ListTableConfigurationsResponse convertToListTableConfigurationsResponse(List<TableConfigurationModel> configurations) {
        return new ListTableConfigurationsResponse()
                .setConfigurations(configurations.stream().map(this::buildTableConfigurationDto).toList());
    }

    private TableConfigurationDto buildTableConfigurationDto(TableConfigurationModel config) {
        return new TableConfigurationDto()
                .setName(config.getName())
                .setBatchSize(config.getBatchSize())
                .setRowsCount(config.getRowsCount())
                .setDeleteDataBeforeStart(config.isDeleteDataBeforeStart())
                .setDeleteMode(config.getDeleteMode() != null ? config.getDeleteMode().getName() : null)
                .setClearScript(config.getClearScript());
    }
}
