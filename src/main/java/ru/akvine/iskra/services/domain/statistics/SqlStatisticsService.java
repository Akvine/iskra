package ru.akvine.iskra.services.domain.statistics;

import ru.akvine.iskra.enums.ProcessState;
import ru.akvine.iskra.enums.SqlScriptType;
import ru.akvine.iskra.repositories.entities.SqlStatisticsEntity;
import ru.akvine.iskra.services.domain.statistics.dto.CreateStatisticAction;
import ru.akvine.iskra.services.domain.statistics.dto.SearchStatisticsContext;
import ru.akvine.iskra.services.domain.statistics.dto.UpdateStatisticAction;

import java.util.Collection;
import java.util.List;

public interface SqlStatisticsService {
    List<SqlStatisticsModel> get(SearchStatisticsContext searchContext);

    SqlStatisticsModel create(CreateStatisticAction action);

    SqlStatisticsModel update(UpdateStatisticAction action);

    // TODO: много параметров, завернуть в DTO
    List<SqlStatisticsModel> updateToStatus(String processUuid,
                                      SqlScriptType scriptType,
                                      ProcessState targetState,
                                      Collection<ProcessState> excludeStates);

    SqlStatisticsEntity verifyExists(String uuid);
}
