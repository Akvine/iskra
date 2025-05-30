package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.akvine.iskra.enums.ProcessState;
import ru.akvine.iskra.exceptions.IntegrationException;
import ru.akvine.iskra.services.GeneratorService;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.domain.table.configuration.TableConfigurationModel;
import ru.akvine.iskra.services.domain.table.process.TableProcessModel;
import ru.akvine.iskra.services.domain.table.process.TableProcessService;
import ru.akvine.iskra.services.dto.process.CreateTableProcess;
import ru.akvine.iskra.services.dto.process.UpdateTableProcess;
import ru.akvine.iskra.services.integration.istochnik.IstochnikService;
import ru.akvine.iskra.services.integration.visor.VisorService;

import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class RestGeneratorServiceImpl implements GeneratorService {
    private final VisorService visorService;
    private final IstochnikService istochnikService;
    private final TableProcessService tableProcessService;

    @Value("${update.table.process.on.iteration}")
    private int updateIteration;

    @Override
    public boolean generate(String processUuid, TableModel table) {
        TableConfigurationModel tableConfiguration = table.getConfiguration();

        if (tableConfiguration.isDeleteDataBeforeStart() &&
                StringUtils.isNotBlank(tableConfiguration.getClearScript())) {
            log.info("Execute clear script for table = [{}]", table.getTableName());
            visorService.executeScripts(
                    Set.of(tableConfiguration.getClearScript()),
                    table.getPlan().getConnection()
            );
        }

        CreateTableProcess createTableProcessAction = new CreateTableProcess()
                .setPlanUuid(table.getPlan().getUuid())
                .setProcessUuid(processUuid)
                .setTableName(table.getTableName())
                .setTotalRowsCount(table.getConfiguration().getRowsCount());
        TableProcessModel tableProcess = tableProcessService.create(createTableProcessAction);
        String pid = tableProcess.getPid();
        generateDataInternal(pid, table);
        return true;
    }

    private void generateDataInternal(String pid, TableModel tableModel) {
        UpdateTableProcess updateTableProcessAction = new UpdateTableProcess()
                .setPid(pid);
        TableConfigurationModel configuration = tableModel.getConfiguration();
        int processedRowsCount = 0;

        int processedRowsCountBeforeUpdate = 0;
        int iterationCounter = 1;
        try {
            while (processedRowsCount < configuration.getRowsCount()) {
                log.info("Sending batch count = [{}] for [{}]. Batch size = [{}]",
                        iterationCounter,
                        tableModel.getTableName(),
                        configuration.getBatchSize());
                byte[] table = istochnikService.generatedData(processedRowsCount, tableModel);
                visorService.sendFile(tableModel, table);
                processedRowsCountBeforeUpdate += configuration.getBatchSize();

                if (iterationCounter % updateIteration == 0) {
                    log.info("Update table process with pid = [{}] and table name = [{}]. Processed rows count = [{}]",
                            pid,
                            tableModel.getTableName(),
                            processedRowsCount);
                    updateTableProcessAction.setAddSuccessRowsCount((long) processedRowsCountBeforeUpdate);
                    tableProcessService.update(updateTableProcessAction);
                    processedRowsCountBeforeUpdate = 0;
                }

                processedRowsCount += configuration.getBatchSize();
                iterationCounter++;
            }

            log.info("Table with pid = [{}] and name = [{}] was successfully filled in!", pid, tableModel.getTableName());
            updateTableProcessAction.setCompletedDate(new Date());
            updateTableProcessAction.setState(ProcessState.SUCCESS);
            updateTableProcessAction.setAddSuccessRowsCount(null);
        } catch (IntegrationException exception) {
            updateTableProcessAction
                    .setAddSuccessRowsCount(null)
                    .setErrorMessage(exception.getMessage())
                    .setState(ProcessState.FAILED);
        } finally {
            tableProcessService.update(updateTableProcessAction);
        }
    }
}
