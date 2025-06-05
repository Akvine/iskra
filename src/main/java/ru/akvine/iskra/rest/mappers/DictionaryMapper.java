package ru.akvine.iskra.rest.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.components.SecurityManager;
import ru.akvine.iskra.rest.dto.dictionary.CreateDictionaryRequest;
import ru.akvine.iskra.rest.dto.dictionary.DictionaryDto;
import ru.akvine.iskra.rest.dto.dictionary.DictionaryListResponse;
import ru.akvine.iskra.rest.dto.dictionary.ListDictionariesRequest;
import ru.akvine.iskra.services.domain.dictionary.DictionaryModel;
import ru.akvine.iskra.services.dto.dictionary.CreateDictionary;
import ru.akvine.iskra.services.dto.dictionary.ListDictionaries;
import ru.akvine.iskra.utils.FileUtils;
import ru.akvine.iskra.utils.StringHelper;

import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DictionaryMapper {
    private final SecurityManager securityManager;

    public ListDictionaries convertToListDictionaries(ListDictionariesRequest request) {
        Asserts.isNotNull(request);
        return new ListDictionaries()
                .setNames(request.getNames())
                .setSystem(request.isSystem());
    }

    public DictionaryListResponse convertToDictionaryListResponse(List<DictionaryModel> dictionaries) {
        return new DictionaryListResponse()
                .setDictionaries(dictionaries.stream().map(this::buildDictionaryDto).toList());
    }

    private DictionaryDto buildDictionaryDto(DictionaryModel dictionary) {
        return new DictionaryDto()
                .setUuid(dictionary.getUuid())
                .setName(dictionary.getName())
                .setLanguage(dictionary.getLanguage().toString())
                .setDescription(dictionary.getDescription())
                .setSystem(dictionary.isSystem())
                .setElements(dictionary.getValues().size())
                .setValues(StringHelper.replaceAroundMiddle(
                        dictionary.getValues().stream().toList(), 3));
    }

    public CreateDictionary convertToCreateDictionary(CreateDictionaryRequest request) {
        return new CreateDictionary()
                .setUserUuid(securityManager.getCurrentUser().getUuid())
                .setName(request.getName())
                .setLanguage(request.getLanguage())
                .setDescription(request.getDescription())
                .setValues(request.getValues());
    }

    public CreateDictionary convertToCreateDictionary(MultipartFile file,
                                                      String name,
                                                      String locale,
                                                      String description) {
        return new CreateDictionary()
                .setName(name)
                .setValues(new HashSet<>(FileUtils.parseValues(FileUtils.extractInputStream(file))))
                .setLanguage(locale)
                .setDescription(description);
    }
}
