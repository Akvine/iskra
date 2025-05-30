package ru.akvine.iskra.repositories;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.iskra.repositories.entities.TableEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TableRepository extends JpaRepository<TableEntity, Long> {
    @Query("from TableEntity te " +
            "where te.plan.uuid = :uuid")
    @EntityGraph(attributePaths = "columns")
    List<TableEntity> findAll(@Param("uuid") String planUuid);

    @Query("from TableEntity te " +
            "where te.plan.uuid = :uuid " +
            "and " +
            "te.selected = :selected")
    @EntityGraph(attributePaths = "columns")
    List<TableEntity> findAll(@Param("uuid") String planUuid,
                              @Param("selected") boolean selected);

    @Query("from TableEntity te " +
            "where te.plan.uuid = :uuid " +
            "and " +
            "te.name in :tableNames")
    @EntityGraph(attributePaths = "columns")
    List<TableEntity> findAll(@Param("uuid") String planUuid,
                              @Param("tableNames") Collection<String> tableNames);

    @Query("from TableEntity te " +
            "where te.name = :name " +
            "and " +
            "te.plan.uuid = :planUuid")
    Optional<TableEntity> findBy(
            @Param("planUuid") String planUuid,
            @Param("name") String name
    );
}
