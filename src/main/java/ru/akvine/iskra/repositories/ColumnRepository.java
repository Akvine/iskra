package ru.akvine.iskra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.iskra.repositories.entities.ColumnEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ColumnRepository extends JpaRepository<ColumnEntity, Long> {
    @Query("from ColumnEntity ce where ce.table.name in :tableNames")
    List<ColumnEntity> findAll(@Param("tableNames") Collection<String> tableNames);

    @Query("from ColumnEntity ce " +
            "where ce.table.name = :tableName " +
            "and " +
            "ce.uuid in :uuids")
    List<ColumnEntity> findAll(@Param("tableName") String tableName,
                               @Param("uuids") Collection<String> uuids);

    @Query("from ColumnEntity ce where ce.uuid = :uuid")
    Optional<ColumnEntity> findByUuid(@Param("uuid") String uuid);

    @Query("from ColumnEntity ce " +
            "where " +
            "ce.table.plan.uuid = :uuid " +
            "and " +
            "ce.referenceInfo.targetColumnNameForForeignKey is not null " +
            "and " +
            "ce.referenceInfo.targetTableNameForForeignKey is not null")
    List<ColumnEntity> findByPlanUuidAndHasReferenceInfo(@Param("uuid") String uuid);
}
