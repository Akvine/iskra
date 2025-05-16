package ru.akvine.iskra.services;

import ru.akvine.iskra.repositories.entities.DictionaryEntity;
import ru.akvine.iskra.services.domain.DictionaryModel;
import ru.akvine.iskra.services.dto.dictionary.CreateDictionary;

import java.util.List;

public interface DictionaryService {
    List<DictionaryModel> list();

    DictionaryModel create(CreateDictionary createDictionary);

    DictionaryEntity verifyExists(String name);
}
