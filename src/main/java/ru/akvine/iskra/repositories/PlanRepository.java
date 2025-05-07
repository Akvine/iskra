package ru.akvine.iskra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.iskra.repositories.entities.PlanEntity;

import java.util.Optional;

public interface PlanRepository extends JpaRepository<PlanEntity, Long> {
    @Query("from PlanEntity pe " +
            "where pe.uuid = :uuid " +
            "and " +
            "pe.deleted = false")
    Optional<PlanEntity> findByUuid(@Param("uuid") String uuid);
}
