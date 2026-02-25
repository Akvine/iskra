package ru.akvine.iskra.services.domain.statistics;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.compozit.commons.utils.UUIDGenerator;
import ru.akvine.iskra.enums.ProcessState;
import ru.akvine.iskra.enums.SqlScriptType;
import ru.akvine.iskra.exceptions.statistics.SqlStatisticsNotFoundException;
import ru.akvine.iskra.repositories.SqlStatisticsRepository;
import ru.akvine.iskra.repositories.entities.SqlStatisticsEntity;
import ru.akvine.iskra.services.domain.statistics.dto.CreateStatisticAction;
import ru.akvine.iskra.services.domain.statistics.dto.SearchStatisticsContext;
import ru.akvine.iskra.services.domain.statistics.dto.UpdateStatisticAction;

@Service
@RequiredArgsConstructor
public class SqlStatisticsServiceImpl implements SqlStatisticsService {
    private final SqlStatisticsRepository sqlStatisticsRepository;

    @Override
    public List<SqlStatisticsModel> get(SearchStatisticsContext searchContext) {
        Asserts.isNotNull(searchContext);
        return sqlStatisticsRepository
                .findByTableIdsAndStatesAndActionType(
                        searchContext.getProcessUuid(), searchContext.getStates(), searchContext.getSqlScriptType())
                .stream()
                .map(SqlStatisticsModel::new)
                .toList();
    }

    @Override
    public SqlStatisticsModel create(CreateStatisticAction action) {
        Asserts.isNotNull(action);

        SqlStatisticsEntity entityToSave = new SqlStatisticsEntity()
                .setProcessUuid(action.getProcessUuid())
                .setStartProcessDate(new Date())
                .setUuid(UUIDGenerator.uuidWithoutDashes())
                .setSqlScriptType(action.getScriptType())
                .setTableId(action.getTableId())
                .setTableName(action.getTableName());

        return new SqlStatisticsModel(sqlStatisticsRepository.save(entityToSave));
    }

    @Override
    public SqlStatisticsModel update(UpdateStatisticAction action) {
        Asserts.isNotNull(action);

        SqlStatisticsEntity entityToUpdate = verifyExists(action.getUuid());

        if (action.getState() != null && entityToUpdate.getProcessState() != action.getState()) {
            entityToUpdate.setProcessState(action.getState());
        }

        return new SqlStatisticsModel(sqlStatisticsRepository.save(entityToUpdate));
    }

    @Override
    public List<SqlStatisticsModel> updateToStatus(
            String processUuid,
            SqlScriptType scriptType,
            ProcessState targetState,
            Collection<ProcessState> excludeStates) {
        // TODO : сделать через Criteria API или Query DSL из-за большого числа параметров
        return sqlStatisticsRepository.updateState(targetState, processUuid, scriptType, excludeStates).stream()
                .map(SqlStatisticsModel::new)
                .toList();
    }

    @Override
    public SqlStatisticsEntity verifyExists(String uuid) {
        Asserts.isNotBlank(uuid, "uuid is null");
        return sqlStatisticsRepository.findByUuid(uuid).orElseThrow(() -> {
            String message = "SQL statistics not found by uuid = [" + uuid + "]";
            return new SqlStatisticsNotFoundException(message);
        });
    }
}
