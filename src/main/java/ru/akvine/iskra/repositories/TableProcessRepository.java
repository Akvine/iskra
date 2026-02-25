package ru.akvine.iskra.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.iskra.repositories.entities.TableProcessEntity;

public interface TableProcessRepository extends JpaRepository<TableProcessEntity, Long> {
    @Query("from TableProcessEntity tpe "
            + "where "
            + "tpe.process.uuid = :uuid "
            + "and "
            + "tpe.tableName = :tableName "
            + "and "
            + "tpe.deleted = false")
    Optional<TableProcessEntity> find(@Param("uuid") String processUuid, @Param("tableName") String tableName);

    @Query("from TableProcessEntity tbe "
            + "where "
            + "tbe.process.plan.uuid = :uuid "
            + "and "
            + "tbe.deleted = false")
    List<TableProcessEntity> findAll(@Param("uuid") String planUuid);
}
