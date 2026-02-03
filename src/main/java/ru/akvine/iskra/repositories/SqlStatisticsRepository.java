package ru.akvine.iskra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.iskra.enums.ProcessState;
import ru.akvine.iskra.enums.SqlScriptType;
import ru.akvine.iskra.repositories.entities.SqlStatisticsEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SqlStatisticsRepository extends JpaRepository<SqlStatisticsEntity, Long> {
    @Query("from SqlStatisticsEntity sse where " +
            "sse.processUuid = :processUuid " +
            "and " +
            "sse.processState in :states " +
            "and " +
            "sse.sqlScriptType = :actionType " +
            "and " +
            "sse.deleted = false")
    List<SqlStatisticsEntity> findByTableIdsAndStatesAndActionType(
            @Param("processUuid") String processUuid,
            @Param("states") Collection<ProcessState> states,
            @Param("action") SqlScriptType actionType);

    @Query("from SqlStatisticsEntity sse where " +
            "sse.uuid = :uuid " +
            "and " +
            "deleted = false")
    Optional<SqlStatisticsEntity> findByUuid(@Param("uuid") String uuid);

    @Modifying
    @Query("update SqlStatisticsEntity sse set " +
            "sse.processState = :processState " +
            "where " +
            "sse.processUuid = :processUuid " +
            "and " +
            "sse.sqlScriptType = :scriptType " +
            "and " +
            "sse.processState not in :states")
    List<SqlStatisticsEntity> updateState(@Param("processState") ProcessState targetState,
                    @Param("processUuid") String processUuid,
                    @Param("scriptType") SqlScriptType scriptType,
                    @Param("states") Collection<ProcessState> excludeStates);
}
