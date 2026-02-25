package ru.akvine.iskra.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.iskra.repositories.entities.DictionaryEntity;

public interface DictionaryRepository
        extends JpaRepository<DictionaryEntity, Long>, JpaSpecificationExecutor<DictionaryEntity> {
    @Query("from DictionaryEntity de where de.name in :names")
    List<DictionaryEntity> findAll(@Param("names") Collection<String> names);

    @Query("from DictionaryEntity de "
            + "where de.uuid = :uuid "
            + "and "
            + "de.user.uuid = :userUuid "
            + "and "
            + "de.user.deleted = false")
    Optional<DictionaryEntity> findByUuid(@Param("uuid") String uuid, @Param("userUuid") String userUuid);

    @Query("from DictionaryEntity de " + "where de.uuid = :uuid " + "and " + "de.system = true")
    Optional<DictionaryEntity> findSystem(@Param("uuid") String uuid);

    @Query("from DictionaryEntity de where de.name in :names")
    List<DictionaryEntity> findByNames(@Param("names") Collection<String> names);

    @Query("from DictionaryEntity de where de.system = :system ")
    List<DictionaryEntity> findBy(@Param("system") boolean system);

    @Query("select count(de) from DictionaryEntity de where de.user.uuid = :uuid")
    long count(@Param("uuid") String userUuid);

    @Query("from DictionaryEntity de " + "where de.uuid in :uuids " + "and " + "de.system = true")
    List<DictionaryEntity> findSystemByUuids(@Param("uuids") Collection<String> uuids);

    @Query("from DictionaryEntity de " + "where de.uuid in :uuids " + "and " + "de.user.uuid = :uuid")
    List<DictionaryEntity> findUserByUuids(@Param("uuids") Collection<String> uuids, @Param("uuid") String userUuid);
}
