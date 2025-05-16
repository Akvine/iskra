package ru.akvine.iskra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.iskra.repositories.entities.ColumnConfigurationEntity;

import java.util.List;

public interface ColumnConfigurationRepository extends JpaRepository<ColumnConfigurationEntity, Long> {
    @Query("from ColumnConfigurationEntity cce where cce.column.uuid = :uuid")
    List<ColumnConfigurationEntity> findAll(@Param("uuid") String columnUuid);

    @Query("select count(*) from ColumnConfigurationEntity cce where cce.column.uuid = :uuid")
    int count(@Param("uuid") String columnUuid);
}
