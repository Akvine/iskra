package ru.akvine.iskra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.iskra.repositories.entities.ConnectionEntity;

import java.util.List;
import java.util.Optional;

public interface ConnectionRepository extends JpaRepository<ConnectionEntity, Long> {
    @Query("from ConnectionEntity ce where " +
            "ce.connectionName = :name " +
            "and " +
            "ce.deleted = false")
    Optional<ConnectionEntity> findByConnectionName(@Param("name") String connectionName);

    @Query("from ConnectionEntity ce where " +
            "ce.deleted = false")
    List<ConnectionEntity> findAll();
}
