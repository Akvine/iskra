package ru.akvine.iskra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.iskra.repositories.entities.TableEntity;

import java.util.List;

public interface TableRepository extends JpaRepository<TableEntity, Long> {
    @Query("select count(*) from TableEntity te " +
            "where te.plan.uuid = :uuid")
    int countByPlan(@Param("uuid") String planUuid);

    @Query("from TableEntity te " +
            "where te.plan.uuid = :uuid")
    List<TableEntity> findAll(@Param("uuid") String planUuid);
}
