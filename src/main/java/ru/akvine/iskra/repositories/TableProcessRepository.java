package ru.akvine.iskra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.iskra.repositories.entities.TableProcessEntity;

import java.util.List;
import java.util.Optional;

public interface TableProcessRepository extends JpaRepository<TableProcessEntity, Long> {
    @Query("from TableProcessEntity tbe " +
            "where " +
            "tbe.pid = :pid " +
            "and " +
            "tbe.deleted = false")
    Optional<TableProcessEntity> find(@Param("pid") String pid);

    @Query("from TableProcessEntity tpe " +
            "where " +
            "tpe.processUuid = :uuid " +
            "and " +
            "tpe.tableName = :tableName " +
            "and " +
            "tpe.deleted = false")
    Optional<TableProcessEntity> find(@Param("uuid") String processUuid,
                                      @Param("tableName") String tableName);

    @Query("from TableProcessEntity tbe " +
            "where " +
            "tbe.plan.uuid = :uuid " +
            "and " +
            "tbe.deleted = false")
    List<TableProcessEntity> findAll(@Param("uuid") String planUuid);
}
