package ru.akvine.iskra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.iskra.repositories.entities.ColumnConfigurationEntity;

import java.util.Optional;

public interface ColumnConfigurationRepository extends JpaRepository<ColumnConfigurationEntity, Long> {
    @Query("from ColumnConfigurationEntity cce where cce.column.uuid = :uuid")
    Optional<ColumnConfigurationEntity> findAll(@Param("uuid") String columnUuid);
}
