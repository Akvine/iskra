package ru.akvine.iskra.services.domain.table.process;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.enums.ProcessState;
import ru.akvine.iskra.exceptions.process.TableProcessNotFoundException;
import ru.akvine.iskra.repositories.TableProcessRepository;
import ru.akvine.iskra.repositories.entities.PlanEntity;
import ru.akvine.iskra.repositories.entities.TableProcessEntity;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.domain.table.process.dto.CreateTableProcess;
import ru.akvine.iskra.services.domain.table.process.dto.ListTableProcess;
import ru.akvine.iskra.services.domain.table.process.dto.UpdateTableProcess;
import ru.akvine.iskra.utils.StringHelper;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TableProcessServiceImpl implements TableProcessService {
    private final TableProcessRepository tableProcessRepository;
    private final PlanService planService;

    @Override
    public TableProcessModel create(CreateTableProcess createTableProcess) {
        Asserts.isNotNull(createTableProcess);

        PlanEntity process = planService.verifyExists(createTableProcess.getPlanUuid(), createTableProcess.getUserUuid());

        TableProcessEntity entityToCreate = new TableProcessEntity()
                .setTotalRowsCount(createTableProcess.getTotalRowsCount())
                .setProcessUuid(createTableProcess.getProcessUuid())
                .setTableName(createTableProcess.getTableName())
                .setStartedDate(new Date())
                .setProcessState(ProcessState.IN_PROGRESS)
                .setPlan(process);
        return new TableProcessModel(tableProcessRepository.save(entityToCreate));
    }

    @Override
    public TableProcessModel update(UpdateTableProcess updateTableProcess) {
        Asserts.isNotNull(updateTableProcess);

        TableProcessEntity tableProcessToUpdate;
        if (StringUtils.isNotBlank(updateTableProcess.getPid())) {
            tableProcessToUpdate = verifyExists(updateTableProcess.getPid());
        } else {
            tableProcessToUpdate = verifyExists(updateTableProcess.getProcessUuid(), updateTableProcess.getTableName());
        }

        ProcessState processState = updateTableProcess.getState();
        String errorMessage = updateTableProcess.getErrorMessage();
        Date completedDate = updateTableProcess.getCompletedDate();

        if (processState != null &&
                !tableProcessToUpdate.getProcessState().equals(processState)) {
            tableProcessToUpdate.setProcessState(processState);
        }

        if (StringUtils.isNotBlank(errorMessage)) {
            tableProcessToUpdate.setErrorMessage(StringHelper.trim(errorMessage, 512, true));
        }

        if (completedDate != null) {
            tableProcessToUpdate.setCompletedDate(completedDate);
        }

        if (updateTableProcess.getAddSuccessRowsCount() != null) {
            tableProcessToUpdate.setSuccessRowsCount(
                    tableProcessToUpdate.getSuccessRowsCount() + updateTableProcess.getAddSuccessRowsCount());
        }

        tableProcessToUpdate.setUpdatedDate(new Date());
        return new TableProcessModel(tableProcessRepository.save(tableProcessToUpdate));
    }

    @Override
    public TableProcessModel get(String pid) {
        return new TableProcessModel(verifyExists(pid));
    }

    @Override
    public TableProcessModel get(String processUuid, String tableName) {
        return new TableProcessModel(verifyExists(processUuid, tableName));
    }

    @Override
    public TableProcessEntity verifyExists(String byProcessUuid, String byTableName) {
        Asserts.isNotNull(byProcessUuid);
        Asserts.isNotNull(byTableName);
        return tableProcessRepository
                .find(byProcessUuid, byTableName)
                .orElseThrow(() -> {
                    String errorMessage = String.format(
                            "Table process not found by processUuid = [%s] and table name = [%s]",
                            byProcessUuid, byTableName);
                    return new TableProcessNotFoundException(errorMessage);
                });
    }

    @Override
    public List<TableProcessModel> list(ListTableProcess listTableProcess) {
        Asserts.isNotNull(listTableProcess);
        List<TableProcessModel> tableProcessModels = tableProcessRepository
                .findAll(listTableProcess.getPlanUuid()).stream()
                .map(TableProcessModel::new)
                .toList();

        // TODO : сделать через Criteria API или Query DSL, т.к. выгружать в память - не самая лучшая идея, особенно если много таблиц
        if (StringUtils.isNotBlank(listTableProcess.getProcessUuid())) {
            tableProcessModels = tableProcessModels.stream()
                    .filter(tableProcess -> tableProcess.getProcessUuid().equals(listTableProcess.getProcessUuid()))
                    .toList();
        }
        return tableProcessModels;
    }

    @Override
    public TableProcessEntity verifyExists(String byPid) {
        Asserts.isNotNull(byPid);
        return tableProcessRepository
                .find(byPid)
                .orElseThrow(() -> {
                    String errorMessage = String.format("Table process not found by pid = [%s]", byPid);
                    return new TableProcessNotFoundException(errorMessage);
                });
    }
}
