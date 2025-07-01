package ru.akvine.iskra.services.domain.regex;

import org.springframework.transaction.annotation.Transactional;
import ru.akvine.iskra.repositories.entities.RegexEntity;
import ru.akvine.iskra.services.domain.regex.dto.CreateRegex;

import java.util.List;

public interface RegexService {
    @Transactional
    List<RegexModel> list(String userUuid);

    @Transactional
    RegexModel create(CreateRegex action);

    RegexEntity verifyExistsByName(String name, String userUuid);
}
