package ru.akvine.iskra.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.iskra.repositories.entities.config.ColumnConfigurationEntity;

public interface ColumnConfigurationRepository extends JpaRepository<ColumnConfigurationEntity, Long> {
    @Query("from ColumnConfigurationEntity cce where cce.column.uuid = :uuid")
    List<ColumnConfigurationEntity> findAll(@Param("uuid") String columnUuid);

    @Query("select count(*) from ColumnConfigurationEntity cce where cce.column.uuid = :uuid")
    int count(@Param("uuid") String columnUuid);

    @Query("from ColumnConfigurationEntity cce " + "where cce.column.uuid = :uuid " + "and " + "cce.name = :name")
    Optional<ColumnConfigurationEntity> findBy(@Param("uuid") String columnUuid, @Param("name") String name);

    @Query("from ColumnConfigurationEntity cce "
            + "where cce.column.columnName = :columnName "
            + "and "
            + "cce.column.table.name = :tableName "
            + "and "
            + "cce.selected = true "
            + "and "
            + "cce.column.table.plan.uuid = :uuid")
    Optional<ColumnConfigurationEntity> findSelected(
            @Param("columnName") String columnName,
            @Param("tableName") String tableName,
            @Param("uuid") String planUuid);

    @Query("from ColumnConfigurationEntity cce " + "where cce.column.uuid in :uuids")
    List<ColumnConfigurationEntity> findByColumnsUuids(@Param("uuids") Collection<String> uuids);
}
