package ru.akvine.iskra.rest.configuration;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.mappers.configuration.TableConfigurationMapper;
import ru.akvine.iskra.rest.dto.configuration.table.CreateTableConfigurationRequest;
import ru.akvine.iskra.rest.dto.configuration.table.UpdateTableConfigurationRequest;
import ru.akvine.iskra.rest.meta.configuration.TableConfigurationControllerMeta;
import ru.akvine.iskra.rest.validators.TableConfigurationValidator;
import ru.akvine.iskra.services.domain.table.configuration.TableConfigurationService;
import ru.akvine.iskra.services.domain.table.configuration.TableConfigurationModel;
import ru.akvine.iskra.services.dto.configuration.table.CreateTableConfiguration;
import ru.akvine.iskra.services.dto.configuration.table.UpdateTableConfiguration;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TableConfigurationController implements TableConfigurationControllerMeta {
    private final TableConfigurationValidator tableConfigurationValidator;
    private final TableConfigurationMapper tableConfigurationMapper;
    private final TableConfigurationService tableConfigurationService;

    @Override
    public Response create(@RequestBody @Valid CreateTableConfigurationRequest request) {
        tableConfigurationValidator.verifyCreateTableConfigurationRequest(request);
        CreateTableConfiguration action = tableConfigurationMapper.convertToCreateTableConfiguration(request);
        TableConfigurationModel createdConfig = tableConfigurationService.create(action);
        return tableConfigurationMapper.convertToListTableConfigurationsResponse(List.of(createdConfig));
    }

    @Override
    public Response update(UpdateTableConfigurationRequest request) {
        tableConfigurationValidator.verifyUpdateTableConfigurationRequest(request);
        UpdateTableConfiguration action = tableConfigurationMapper.convertToUpdateTableConfiguration(request);
        TableConfigurationModel config = tableConfigurationService.update(action);
        return tableConfigurationMapper.convertToListTableConfigurationsResponse(List.of(config));
    }
}
