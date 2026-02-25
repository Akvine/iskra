package ru.akvine.iskra.services.domain.dictionary;

import java.util.Collection;
import java.util.List;
import ru.akvine.iskra.repositories.entities.DictionaryEntity;
import ru.akvine.iskra.services.domain.dictionary.dto.CreateDictionary;
import ru.akvine.iskra.services.domain.dictionary.dto.ListDictionaries;

public interface DictionaryService {
    List<DictionaryModel> list(ListDictionaries action);

    DictionaryModel create(CreateDictionary createDictionary);

    void delete(String uuid, String userUuid);

    DictionaryEntity verifySystemExists(String uuid);

    List<DictionaryEntity> verifySystemExists(Collection<String> uuids);

    DictionaryEntity verifyUserExists(String uuid, String userUuid);

    List<DictionaryEntity> verifyUserExists(Collection<String> uuids, String userUuid);
}
