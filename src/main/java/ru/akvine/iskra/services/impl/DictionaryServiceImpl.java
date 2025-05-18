package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.enums.Language;
import ru.akvine.iskra.exceptions.dictionary.DictionaryNotFoundException;
import ru.akvine.iskra.repositories.DictionaryRepository;
import ru.akvine.iskra.repositories.entities.DictionaryEntity;
import ru.akvine.iskra.services.DictionaryService;
import ru.akvine.iskra.services.domain.DictionaryModel;
import ru.akvine.iskra.services.dto.dictionary.CreateDictionary;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DictionaryServiceImpl implements DictionaryService {
    private final DictionaryRepository dictionaryRepository;

    @Override
    public List<DictionaryModel> list() {
        return dictionaryRepository.findAll().stream()
                .map(DictionaryModel::new)
                .toList();
    }

    @Override
    public DictionaryModel create(CreateDictionary createDictionary) {
        Asserts.isNotNull(createDictionary);

        DictionaryEntity dictionaryToCreate = new DictionaryEntity()
                .setName(createDictionary.getName())
                .setDescription(createDictionary.getDescription())
                .setValues(String.join(",", createDictionary.getValues()));
        if (createDictionary.getLanguage() != null) {
            dictionaryToCreate.setLanguage(Language.from(createDictionary.getLanguage()));
        }

        return new DictionaryModel(dictionaryRepository.save(dictionaryToCreate));
    }

    @Override
    public DictionaryEntity verifyExists(String name) {
        Asserts.isNotNull(name);
        return dictionaryRepository
                .findByName(name)
                .orElseThrow(() -> {
                    String errorMessage = "Dictionary with name = [" + name + "] not found!";
                    return new DictionaryNotFoundException(errorMessage);
                });
    }
}
