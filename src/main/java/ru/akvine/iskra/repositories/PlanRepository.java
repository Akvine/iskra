package ru.akvine.iskra.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.iskra.repositories.entities.PlanEntity;

public interface PlanRepository extends JpaRepository<PlanEntity, Long> {
    @Query("from PlanEntity pe " + "where pe.user.uuid = :userUuid " + "and " + "pe.user.deleted = false")
    List<PlanEntity> findAll(@Param("userUuid") String userUuid);

    @Query("from PlanEntity pe "
            + "where pe.uuid = :uuid "
            + "and "
            + "pe.user.uuid = :userUuid "
            + "and "
            + "user.deleted = false")
    Optional<PlanEntity> findByUuid(@Param("uuid") String uuid, @Param("userUuid") String userUuid);
}
