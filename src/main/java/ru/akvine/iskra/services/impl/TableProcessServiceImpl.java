package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.enums.ProcessState;
import ru.akvine.iskra.exceptions.process.TableProcessNotFoundException;
import ru.akvine.iskra.repositories.TableProcessRepository;
import ru.akvine.iskra.repositories.entities.PlanEntity;
import ru.akvine.iskra.repositories.entities.TableProcessEntity;
import ru.akvine.iskra.services.PlanService;
import ru.akvine.iskra.services.TableProcessService;
import ru.akvine.iskra.services.domain.TableProcessModel;
import ru.akvine.iskra.services.dto.process.CreateTableProcess;
import ru.akvine.iskra.services.dto.process.ListTableProcess;
import ru.akvine.iskra.services.dto.process.UpdateTableProcess;
import ru.akvine.iskra.utils.PIDGenerator;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TableProcessServiceImpl implements TableProcessService {
    @Value("${process.pid.generation.length}")
    private int pidLength;

    private final TableProcessRepository tableProcessRepository;

    private final PlanService planService;

    @Override
    public TableProcessModel create(CreateTableProcess createTableProcess) {
        Asserts.isNotNull(createTableProcess);

        PlanEntity process = planService.verifyExists(createTableProcess.getPlanUuid());

        TableProcessEntity entityToCreate = new TableProcessEntity()
                .setProcessUuid(createTableProcess.getProcessUuid())
                .setPid(PIDGenerator.generate(pidLength))
                .setTableName(createTableProcess.getTableName())
                .setStartedDate(new Date())
                .setProcessState(ProcessState.IN_PROGRESS)
                .setPlan(process);
        return new TableProcessModel(tableProcessRepository.save(entityToCreate));
    }

    @Override
    public TableProcessModel update(UpdateTableProcess updateTableProcess) {
        Asserts.isNotNull(updateTableProcess);

        TableProcessEntity tableProcessToUpdate = verifyExists(updateTableProcess.getPid());
        ProcessState processState = updateTableProcess.getState();
        String errorMessage = updateTableProcess.getErrorMessage();
        Date completedDate = updateTableProcess.getCompletedDate();

        if (processState != null &&
                !tableProcessToUpdate.getProcessState().equals(processState)) {
            tableProcessToUpdate.setProcessState(processState);
        }

        if (StringUtils.isNotBlank(errorMessage)) {
            tableProcessToUpdate.setErrorMessage(errorMessage);
        }

        if (completedDate != null) {
            tableProcessToUpdate.setCompletedDate(completedDate);
        }

        tableProcessToUpdate.setUpdatedDate(new Date());
        return new TableProcessModel(tableProcessRepository.save(tableProcessToUpdate));
    }

    @Override
    public TableProcessModel get(String pid) {
        return new TableProcessModel(verifyExists(pid));
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
