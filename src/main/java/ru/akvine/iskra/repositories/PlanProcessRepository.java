package ru.akvine.iskra.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.iskra.repositories.entities.PlanProcessEntity;

public interface PlanProcessRepository extends JpaRepository<PlanProcessEntity, Long> {
    @Query("from PlanProcessEntity ppe where ppe.uuid = :uuid " + "and " + "ppe.deleted = false")
    Optional<PlanProcessEntity> findByUuid(@Param("uuid") String uuid);

    @Query("from PlanProcessEntity ppe where ppe.plan.uuid = :uuid "
            + "and "
            + "ppe.processState in ('STOPPED', 'FAILED')"
            + "and "
            + "ppe.deleted = false "
            + "order by ppe.createdDate desc")
    Optional<PlanProcessEntity> findLastStoppedOrFailed(@Param("uuid") String planUuid);
}
