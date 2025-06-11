package ru.akvine.iskra.services.domain.dictionary;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.compozit.commons.utils.UUIDGenerator;
import ru.akvine.iskra.enums.Language;
import ru.akvine.iskra.exceptions.dictionary.DictionaryMaxCountException;
import ru.akvine.iskra.exceptions.dictionary.DictionaryNotFoundException;
import ru.akvine.iskra.repositories.DictionaryRepository;
import ru.akvine.iskra.repositories.dto.DictionariesFilter;
import ru.akvine.iskra.repositories.entities.DictionaryEntity;
import ru.akvine.iskra.repositories.entities.UserEntity;
import ru.akvine.iskra.repositories.specifications.DictionarySpecification;
import ru.akvine.iskra.services.UserService;
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
    private final UserService userService;
    private final DictionarySpecification dictionarySpecification;
    private static final Map<String, DictionaryModel> DICTIONARIES = new ConcurrentHashMap<>();

    @Value("${dictionaries.cache.enabled}")
    private boolean cacheEnabled;
    @Value("${max.dictionaries.per.user}")
    private int maxCountPerUser;

    @PostConstruct
    public void init() {
        if (cacheEnabled) {
            log.info("Dictionaries cache enabled. Start loading...");

            List<DictionaryEntity> dictionaries = dictionaryRepository.findBy(true);
            for (DictionaryEntity dictionaryToLoad : dictionaries) {
                log.info("{} --> loading data is complete!", dictionaryToLoad.getName());
                DICTIONARIES.put(dictionaryToLoad.getUuid(), new DictionaryModel(dictionaryToLoad));
            }

            log.info("Dictionaries cache loaded successful!");
        } else {
            log.info("Dictionaries cache disabled");
        }
    }

    @Override
    public List<DictionaryModel> list(ListDictionaries action) {
        Asserts.isNotNull(action);

        List<DictionaryModel> systemDictionaries = List.of();
        if (action.isSystem()) {
            if (cacheEnabled) {
                systemDictionaries = new ArrayList<>(DICTIONARIES.values());
            } else {
                systemDictionaries = dictionaryRepository.findBy(true).stream()
                        .map(DictionaryModel::new).toList();
            }
        }

        List<DictionaryModel> userDictionaries = List.of();
        if (CollectionUtils.isNotEmpty(action.getNames())) {
            DictionariesFilter filter = new DictionariesFilter()
                    .setNames(action.getNames())
                    .setUserUuid(action.getUserUuid());

            Specification<DictionaryEntity> specification = dictionarySpecification.build(filter);

            PageRequest pageRequest = PageRequest.of(action.getPageInfo().getPage(), action.getPageInfo().getCount());
            userDictionaries = dictionaryRepository
                    .findAll(specification, pageRequest).stream()
                    .map(DictionaryModel::new)
                    .toList();
        }

        return ListUtils.union(userDictionaries, systemDictionaries);
    }

    @Override
    @Transactional
    public DictionaryModel create(CreateDictionary createDictionary) {
        Asserts.isNotNull(createDictionary);

        UserEntity owner = userService.verifyExistsByUuid(createDictionary.getUserUuid());

        if (maxCountPerUser == dictionaryRepository.count(owner.getUuid())) {
            String errorMessage = String.format(
                    "Max count dictionaries = [%s] per user was exceeded! Remove unused dictionaries",
                    maxCountPerUser
            );
            throw new DictionaryMaxCountException(errorMessage);
        }
        DictionaryEntity dictionaryToCreate = new DictionaryEntity()
                .setUuid(UUIDGenerator.uuidWithoutDashes())
                .setName(createDictionary.getName())
                .setDescription(createDictionary.getDescription())
                .setValues(String.join(",", createDictionary.getValues()))
                .setUser(owner);
        if (createDictionary.getLanguage() != null) {
            dictionaryToCreate.setLanguage(Language.from(createDictionary.getLanguage()));
        }

        return new DictionaryModel(dictionaryRepository.save(dictionaryToCreate));
    }

    @Override
    public DictionaryEntity verifySystemExists(String uuid) {

        return dictionaryRepository
                .findSystem(uuid)
                .orElseThrow(() -> {
                    String errorMessage = String.format(
                            "System dictionary with uuid = [%s] not found!",
                            uuid
                    );
                    return new DictionaryNotFoundException(errorMessage);
                });
    }

    @Override
    public DictionaryEntity verifyUserExists(String uuid, String userUuid) {
        Asserts.isNotBlank(uuid, "uuid is blank!");
        Asserts.isNotBlank(userUuid, "userUuid is blank!");
        return dictionaryRepository
                .findByUuid(uuid, userUuid)
                .orElseThrow(() -> {
                    String errorMessage = String.format(
                            "Dictionary with uuid = [%s] not found for user = [%s]!",
                            uuid, userUuid
                    );
                    return new DictionaryNotFoundException(errorMessage);
                });
    }
}
