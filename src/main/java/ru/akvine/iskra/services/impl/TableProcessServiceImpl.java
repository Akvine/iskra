package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.TableConfig;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.enums.ProcessState;
import ru.akvine.iskra.exceptions.TableProcessNotFoundException;
import ru.akvine.iskra.repositories.entities.TableProcessEntity;
import ru.akvine.iskra.repositories.TableProcessRepository;
import ru.akvine.iskra.services.TableProcessService;
import ru.akvine.iskra.services.domain.TableProcessModel;
import ru.akvine.iskra.utils.PIDGenerator;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TableProcessServiceImpl implements TableProcessService {
    @Value("${process.pid.generation.length}")
    private int pidLength;

    private final TableProcessRepository tableProcessRepository;

    @Override
    public TableProcessModel create(TableName tableName, TableConfig config) {
        Asserts.isNotNull(config);

        TableProcessEntity entityToCreate = new TableProcessEntity()
                .setPid(PIDGenerator.generate(pidLength))
                .setTableName(tableName.getName())
                .setStartedDate(new Date())
                .setProcessState(ProcessState.IN_PROGRESS);
        return new TableProcessModel(tableProcessRepository.save(entityToCreate));
    }

    @Override
    public TableProcessModel get(String pid) {
        return new TableProcessModel(verifyExists(pid));
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
