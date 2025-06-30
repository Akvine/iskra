package ru.akvine.iskra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.iskra.repositories.entities.config.TableConfigurationEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TableConfigurationRepository extends JpaRepository<TableConfigurationEntity, Long> {
    @Query("from TableConfigurationEntity tce where " +
            "tce.table.name = :tableName")
    Optional<TableConfigurationEntity> findBy(@Param("tableName") String tableName);

    @Query("from TableConfigurationEntity tce where " +
            "tce.table.name in :tableNames " +
            "and " +
            "tce.table.plan.uuid = :planUuid")
    List<TableConfigurationEntity> findBy(@Param("planUuid") String planUuid,
                                          @Param("tableNames") Collection<String> tableNames);
}
