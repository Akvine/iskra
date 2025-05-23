package ru.akvine.iskra.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.akvine.iskra.repositories.entities.config.TableConfigurationEntity;

public interface TableConfigurationRepository extends JpaRepository<TableConfigurationEntity, Long> {
}
