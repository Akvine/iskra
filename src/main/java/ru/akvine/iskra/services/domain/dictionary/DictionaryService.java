package ru.akvine.iskra.services.domain.dictionary;

import ru.akvine.iskra.repositories.entities.DictionaryEntity;
import ru.akvine.iskra.services.dto.dictionary.CreateDictionary;
import ru.akvine.iskra.services.dto.dictionary.ListDictionaries;

import java.util.List;

public interface DictionaryService {
    List<DictionaryModel> list(ListDictionaries action);

    DictionaryModel create(CreateDictionary createDictionary);

    DictionaryEntity verifyExists(String name);
}
