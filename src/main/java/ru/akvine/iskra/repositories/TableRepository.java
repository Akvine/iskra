package ru.akvine.iskra.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.iskra.repositories.entities.TableEntity;

import java.util.List;

public interface TableRepository extends JpaRepository<TableEntity, Long> {
    @Query("from TableEntity te " +
            "where te.plan.uuid = :uuid")
    @EntityGraph(attributePaths = "columns")
    List<TableEntity> findAll(@Param("uuid") String planUuid);
}
