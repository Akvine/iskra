package ru.akvine.iskra.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.iskra.repositories.entities.TableEntity;

public interface TableRepository extends JpaRepository<TableEntity, Long> {
    @Query("from TableEntity te "
            + "where te.plan.uuid = :uuid "
            + "and "
            + "te.plan.user.uuid = :userUuid "
            + "and "
            + "te.plan.user.deleted = false")
    @EntityGraph(attributePaths = "columns")
    List<TableEntity> findAll(@Param("uuid") String planUuid, @Param("userUuid") String userUuid);

    @Query("from TableEntity te "
            + "where te.plan.uuid = :uuid "
            + "and "
            + "te.selected = :selected "
            + "and "
            + "te.plan.user.uuid = :userUuid "
            + "and "
            + "te.plan.user.deleted = false")
    @EntityGraph(attributePaths = "columns")
    List<TableEntity> findAll(
            @Param("userUuid") String userUuid, @Param("uuid") String planUuid, @Param("selected") boolean selected);

    @Query("from TableEntity te " + "where te.plan.uuid = :uuid " + "and " + "te.name in :tableNames")
    @EntityGraph(attributePaths = "columns")
    List<TableEntity> findAll(
            @Param("uuid") String planUuid,
            @Param("tableNames") Collection<String> tableNames,
            @Param("userUuid") String userUuid);

    @Query("from TableEntity te " + "where te.name = :name " + "and " + "te.plan.uuid = :planUuid")
    Optional<TableEntity> findBy(@Param("planUuid") String planUuid, @Param("name") String name);

    @Query("from TableEntity te " + "where te.plan.uuid = :uuid " + "and " + "te.id in :tableIds")
    List<TableEntity> findByPlanUuidAndTableIds(
            @Param("uuid") String uuid, @Param("tableIds") Collection<Long> tableIds);
}
