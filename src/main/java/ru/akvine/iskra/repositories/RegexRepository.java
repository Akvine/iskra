package ru.akvine.iskra.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.iskra.repositories.entities.RegexEntity;

public interface RegexRepository extends JpaRepository<RegexEntity, Long> {
    @Query("from RegexEntity re where "
            + "re.user.uuid = :uuid "
            + "and "
            + "re.name = :name "
            + "and "
            + "re.deleted = false")
    Optional<RegexEntity> find(@Param("uuid") String userUuid, @Param("name") String name);

    @Query("from RegexEntity re where " + "re.user.uuid = :uuid " + "and " + "re.deleted = false")
    List<RegexEntity> find(@Param("uuid") String userUuid);
}
