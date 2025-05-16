package ru.akvine.iskra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.akvine.iskra.repositories.entities.DictionaryEntity;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DictionaryRepository extends JpaRepository<DictionaryEntity, Long> {
    @Query("from DictionaryEntity de where de.name in :names")
    List<DictionaryEntity> findAll(@Param("names") Collection<String> names);

    @Query("from DictionaryEntity de where de.name = :name")
    Optional<DictionaryEntity> findByName(@Param("name") String name);
}
