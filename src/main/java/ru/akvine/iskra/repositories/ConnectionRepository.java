package ru.akvine.iskra.repositories;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.iskra.repositories.entities.ConnectionEntity;

public interface ConnectionRepository extends JpaRepository<ConnectionEntity, Long> {
    @Query("from ConnectionEntity ce where "
            + "ce.connectionName = :name "
            + "and "
            + "ce.deleted = false "
            + "and "
            + "ce.user.uuid = :userUuid "
            + "and "
            + "user.deleted = false")
    @EntityGraph(attributePaths = "user")
    Optional<ConnectionEntity> findByConnectionName(
            @Param("name") String connectionName, @Param("userUuid") String userUuid);

    @Query("from ConnectionEntity ce where "
            + "ce.deleted = false "
            + "and "
            + "ce.user.uuid = :userUuid "
            + "and "
            + "ce.user.deleted = false")
    @EntityGraph(attributePaths = "user")
    List<ConnectionEntity> findAll(@Param("userUuid") String userUuid);
}
