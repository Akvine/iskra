package ru.akvine.iskra.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.iskra.repositories.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("from UserEntity ue where " + "ue.username = :username " + "and " + "ue.deleted = false")
    Optional<UserEntity> findByUsername(@Param("username") String username);

    @Query("from UserEntity ue where " + "ue.email = :email " + "and " + "ue.deleted = false")
    Optional<UserEntity> findByEmail(@Param("email") String email);

    @Query("from UserEntity ue where " + "ue.uuid = :uuid " + "and " + "ue.deleted = false")
    Optional<UserEntity> findByUuid(@Param("uuid") String uuid);
}
