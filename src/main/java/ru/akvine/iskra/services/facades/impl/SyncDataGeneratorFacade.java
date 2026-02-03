package ru.akvine.iskra.services.facades.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import ru.akvine.iskra.enums.ProcessState;
import ru.akvine.iskra.services.GeneratorCacheService;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.domain.table.configuration.TableConfigurationModel;
import ru.akvine.iskra.services.domain.table.process.TableProcessModel;
import ru.akvine.iskra.services.domain.table.process.TableProcessService;
import ru.akvine.iskra.services.domain.table.process.dto.UpdateTableProcess;
import ru.akvine.iskra.services.facades.DataGeneratorFacade;
import ru.akvine.iskra.services.integration.istochnik.IstochnikService;
import ru.akvine.iskra.services.integration.visor.VisorService;

import java.util.Date;

@RequiredArgsConstructor
@Slf4j
public class SyncDataGeneratorFacade implements DataGeneratorFacade {
    private final VisorService visorService;
    private final IstochnikService istochnikService;
    private final TableProcessService tableProcessService;
    private final GeneratorCacheService generatorCacheService;

    @Value("${update.table.process.on.iteration}")
    private int updateIteration;

    @Override
    public void generate(String processUuid, TableModel tableModel, boolean resume) {
        TableProcessModel tableProcess = tableProcessService.get(processUuid, tableModel.getTableName());

        UpdateTableProcess updateTableProcessAction = new UpdateTableProcess()
                .setProcessUuid(tableProcess.getProcessUuid());
        TableConfigurationModel configuration = tableModel.getConfiguration();

        int processedRowsCount = (int) tableProcess.getSuccessRowsCount();

        int processedRowsCountBeforeUpdate = 0;
        int iterationCounter = 1;
        try {
            while (processedRowsCount < configuration.getRowsCount()) {
                if (isStopped(tableModel.getPlan().getUuid())) {
                    updateTableProcessAction.setAddSuccessRowsCount((long) processedRowsCountBeforeUpdate);
                    updateTableProcessAction.setState(ProcessState.STOPPED);
                    tableProcessService.update(updateTableProcessAction);
                    Thread.currentThread().interrupt();
                    return;
                }

                log.info("Sending batch count = [{}] for [{}]. Batch size = [{}]",
                        iterationCounter,
                        tableModel.getTableName(),
                        configuration.getBatchSize());
                byte[] table = istochnikService.generatedData(processedRowsCount, tableModel);
                if (isStopped(tableModel.getPlan().getUuid())) {
                    updateTableProcessAction.setAddSuccessRowsCount((long) processedRowsCountBeforeUpdate);
                    updateTableProcessAction.setState(ProcessState.STOPPED);
                    tableProcessService.update(updateTableProcessAction);
                    Thread.currentThread().interrupt();
                    return;
                }

                visorService.sendFile(tableModel, table);
                processedRowsCountBeforeUpdate += configuration.getBatchSize();

                if (iterationCounter % updateIteration == 0) {
                    log.info("Update table process with uuid = [{}] and table name = [{}]. Processed rows count = [{}]",
                            processUuid,
                            tableModel.getTableName(),
                            processedRowsCount);
                    updateTableProcessAction.setAddSuccessRowsCount((long) processedRowsCountBeforeUpdate);
                    tableProcessService.update(updateTableProcessAction);
                    processedRowsCountBeforeUpdate = 0;
                }

                processedRowsCount += configuration.getBatchSize();
                iterationCounter++;
            }

            log.info("Table with uuid = [{}] and name = [{}] was successfully filled in!", processUuid, tableModel.getTableName());
            updateTableProcessAction.setCompletedDate(new Date());
            updateTableProcessAction.setState(ProcessState.COMPLETED);
            updateTableProcessAction.setAddSuccessRowsCount(null);
        } catch (Exception exception) {
            updateTableProcessAction
                    .setAddSuccessRowsCount(null)
                    .setErrorMessage(exception.getMessage())
                    .setState(ProcessState.FAILED);
        } finally {
            tableProcessService.update(updateTableProcessAction);
        }
    }

    private boolean isStopped(String planUuid) {
        return generatorCacheService.isStopped(planUuid);
    }
}
