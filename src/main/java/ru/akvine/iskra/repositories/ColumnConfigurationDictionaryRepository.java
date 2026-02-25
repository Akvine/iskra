package ru.akvine.iskra.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.iskra.repositories.entities.config.ColumnConfigurationDictionaryEntity;

public interface ColumnConfigurationDictionaryRepository
        extends JpaRepository<ColumnConfigurationDictionaryEntity, Long> {
    @Query("from ColumnConfigurationDictionaryEntity ccde where "
            + "ccde.columnConfiguration = :columnConfigurationId "
            + "and "
            + "ccde.deleted = false")
    List<ColumnConfigurationDictionaryEntity> findAll(@Param("columnConfigurationId") Long columnConfigurationId);
}
