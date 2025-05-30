package ru.akvine.iskra.services.domain.dictionary;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.enums.Language;
import ru.akvine.iskra.exceptions.dictionary.DictionaryNotFoundException;
import ru.akvine.iskra.repositories.DictionaryRepository;
import ru.akvine.iskra.repositories.entities.DictionaryEntity;
import ru.akvine.iskra.services.dto.dictionary.CreateDictionary;
import ru.akvine.iskra.services.dto.dictionary.ListDictionaries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
@DependsOn("dictionaryLoader")
public class DictionaryServiceImpl implements DictionaryService {
    private final DictionaryRepository dictionaryRepository;
    private static final Map<String, DictionaryModel> DICTIONARIES = new ConcurrentHashMap<>();

    @Value("${dictionaries.cache.enabled}")
    private boolean cacheEnabled;

    @PostConstruct
    public void init() {
        if (cacheEnabled) {
            log.info("Dictionaries cache enabled. Start loading...");

            List<DictionaryEntity> dictionaries = dictionaryRepository.findBy(true);
            for (DictionaryEntity dictionaryToLoad : dictionaries) {
                log.info("{} --> loading data is complete!", dictionaryToLoad.getName());
                DICTIONARIES.put(dictionaryToLoad.getName(), new DictionaryModel(dictionaryToLoad));
            }

            log.info("Dictionaries cache loaded successful!");
        } else {
            log.info("Dictionaries cache disabled");
        }
    }

    @Override
    public List<DictionaryModel> list(ListDictionaries action) {
        Asserts.isNotNull(action);

        // TODO: сделать через Query DSL или Criteria API, а не фильтрацию в памяти, да и кода слишком много
        if (cacheEnabled && action.isSystem()) {

            List<DictionaryModel> collectedDictionaries = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(action.getNames())) {
               action.getNames().forEach(name -> collectedDictionaries.add(DICTIONARIES.get(name)));
            } else {
                return DICTIONARIES.values().stream().toList();
            }

            return collectedDictionaries;
        }

        List<DictionaryModel> dictionaries = List.of();
        if (CollectionUtils.isNotEmpty(action.getNames())) {
            dictionaries = dictionaryRepository.findAll(action.getNames()).stream()
                    .map(DictionaryModel::new)
                    .toList();

            if (action.isSystem()) {
                dictionaries = dictionaries.stream()
                        .filter(DictionaryModel::isSystem)
                        .toList();
            }
        }

        if (action.isSystem()) {
            dictionaries = dictionaryRepository.findBy(true).stream()
                    .map(DictionaryModel::new)
                    .toList();
        }

        return dictionaries;
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
