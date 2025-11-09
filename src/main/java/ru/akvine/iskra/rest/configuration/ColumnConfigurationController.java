package ru.akvine.iskra.rest.configuration;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;
import ru.akvine.iskra.rest.mappers.configuration.ColumnConfigurationMapper;
import ru.akvine.iskra.rest.dto.configuration.column.CreateConfigurationRequest;
import ru.akvine.iskra.rest.dto.configuration.column.SelectConfigurationRequest;
import ru.akvine.iskra.rest.meta.configuration.ColumnConfigurationControllerMeta;
import ru.akvine.iskra.rest.validators.ColumnConfigurationValidator;
import ru.akvine.iskra.services.domain.column.configuration.ColumnConfigurationService;
import ru.akvine.iskra.services.domain.column.configuration.ColumnConfigurationModel;
import ru.akvine.iskra.services.domain.column.configuration.dto.CreateColumnConfiguration;
import ru.akvine.iskra.services.domain.column.configuration.dto.SelectColumnConfiguration;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ColumnConfigurationController implements ColumnConfigurationControllerMeta {
    private final ColumnConfigurationService columnConfigurationService;
    private final ColumnConfigurationMapper columnConfigurationMapper;
    private final ColumnConfigurationValidator columnConfigurationValidator;

    @Override
    public Response list(@PathVariable("columnUuid") String columnUuid) {
        List<ColumnConfigurationModel> models = columnConfigurationService.list(columnUuid);
        return columnConfigurationMapper.mapToConfigurationListResponse(models);
    }

    @Override
    public Response create(@RequestBody @Valid CreateConfigurationRequest request) {
        columnConfigurationValidator.verifyCreateConfigurationRequest(request);
        CreateColumnConfiguration action = columnConfigurationMapper.mapToCreateColumnConfiguration(request);
        ColumnConfigurationModel createdModel = columnConfigurationService.create(action);
        return columnConfigurationMapper.mapToConfigurationListResponse(List.of(createdModel));
    }

    @Override
    public Response select(@RequestBody @Valid SelectConfigurationRequest request) {
        SelectColumnConfiguration selectAction = columnConfigurationMapper.mapToSelectColumnConfiguration(request);
        columnConfigurationService.select(selectAction);
        return new SuccessfulResponse();
    }

    @Override
    public Response generate(@PathVariable("planUuid") String planUuid) {
        List<ColumnConfigurationModel> createdConfigs = columnConfigurationService.generateForExternalRelations(planUuid);
        return columnConfigurationMapper.mapToConfigurationListResponse(createdConfigs);
    }

}
