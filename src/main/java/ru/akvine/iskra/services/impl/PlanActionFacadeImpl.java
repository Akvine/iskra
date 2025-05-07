package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.akvine.compozit.commons.ConnectionDto;
import ru.akvine.compozit.commons.RelationsMatrixDto;
import ru.akvine.compozit.commons.TableConfig;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.enums.ProcessState;
import ru.akvine.iskra.events.GenerateDataEvent;
import ru.akvine.iskra.exceptions.IntegrationException;
import ru.akvine.iskra.services.PlanActionFacade;
import ru.akvine.iskra.services.TableProcessService;
import ru.akvine.iskra.services.domain.TableProcessModel;
import ru.akvine.iskra.services.dto.GenerateDataAction;
import ru.akvine.iskra.services.dto.process.CreateTableProcess;
import ru.akvine.iskra.services.dto.process.UpdateTableProcess;
import ru.akvine.iskra.services.integration.istochnik.IstochnikService;
import ru.akvine.iskra.services.integration.visor.VisorService;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PlanActionFacadeImpl implements PlanActionFacade {
    private final VisorService visorService;
    private final IstochnikService istochnikService;
    private final TableProcessService tableProcessService;

    // TODO: из-за циклической зависимости TableProcessService от PlanService пришлось сделать обработку событий
    @EventListener
    @Async
    public void handleEvent(GenerateDataEvent event) {
        generateData(event.getAction(), event.getConnection());
    }

    @Override
    public void generateData(GenerateDataAction action, ConnectionDto connection) {
        RelationsMatrixDto relationsMatrix = action.getRelationsMatrix();
        Map<TableName, TableConfig> configuration = action.getConfiguration();

        if (CollectionUtils.isEmpty(configuration)) {
            return;
        }

        List<TableName> tableNamesHasNoRelations = relationsMatrix.getRows().stream()
                .filter(row -> !row.hasRelations())
                .map(row -> new TableName(row.getTableName()))
                .toList();

        for (TableName tableName : tableNamesHasNoRelations) {
            TableConfig config = configuration.get(tableName);
            generateData(action.getPlanUuid(), tableName, config, connection);
        }
    }

    @Override
    public TableProcessModel generateData(String processPid,
                                          TableName tableName,
                                          TableConfig config,
                                          ConnectionDto connection) {
        CreateTableProcess createTableProcessAction = new CreateTableProcess()
                .setProcessUuid(processPid)
                .setTableName(tableName)
                .setConfig(config);
        TableProcessModel tableProcess = tableProcessService.create(createTableProcessAction);
        String pid = tableProcess.getPid();
        generateDataInternal(pid, tableName, config, connection);
        return tableProcess;
    }

    private void generateDataInternal(String pid, TableName tableName, TableConfig config, ConnectionDto connection) {
        UpdateTableProcess updateTableProcessAction = new UpdateTableProcess()
                .setPid(pid);
        try {
            byte[] table = istochnikService.generatedData(config);
            visorService.sendFile(tableName, table, config, connection);

            updateTableProcessAction.setCompletedDate(new Date());
            updateTableProcessAction.setState(ProcessState.SUCCESS);
        } catch (IntegrationException exception) {
            updateTableProcessAction
                    .setErrorMessage(exception.getMessage())
                    .setState(ProcessState.FAILED);
        } finally {
            tableProcessService.update(updateTableProcessAction);
        }
    }
}
