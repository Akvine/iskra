package ru.akvine.iskra.rest.mappers;

import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.compozit.commons.PageInfo;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.components.SecurityManager;
import ru.akvine.iskra.rest.dto.dictionary.CreateDictionaryRequest;
import ru.akvine.iskra.rest.dto.dictionary.DictionaryDto;
import ru.akvine.iskra.rest.dto.dictionary.DictionaryListResponse;
import ru.akvine.iskra.rest.dto.dictionary.ListDictionariesRequest;
import ru.akvine.iskra.services.domain.dictionary.DictionaryModel;
import ru.akvine.iskra.services.domain.dictionary.dto.CreateDictionary;
import ru.akvine.iskra.services.domain.dictionary.dto.ListDictionaries;
import ru.akvine.iskra.utils.FileUtils;
import ru.akvine.iskra.utils.StringHelper;

@Component
@RequiredArgsConstructor
public class DictionaryMapper {
    private final SecurityManager securityManager;

    public ListDictionaries mapToListDictionaries(ListDictionariesRequest request) {
        Asserts.isNotNull(request);
        return new ListDictionaries()
                .setPageInfo(new PageInfo(
                        request.getNextPage().getPage(), request.getNextPage().getCount()))
                .setNames(request.getNames())
                .setSystem(request.isSystem());
    }

    public DictionaryListResponse mapToDictionaryListResponse(List<DictionaryModel> dictionaries) {
        return new DictionaryListResponse()
                .setDictionaries(
                        dictionaries.stream().map(this::buildDictionaryDto).toList());
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

    public CreateDictionary mapToCreateDictionary(CreateDictionaryRequest request) {
        return new CreateDictionary()
                .setUserUuid(securityManager.getCurrentUser().getUuid())
                .setName(request.getName())
                .setLanguage(request.getLanguage())
                .setDescription(request.getDescription())
                .setValues(request.getValues());
    }

    public CreateDictionary mapToCreateDictionary(MultipartFile file, String name, String locale, String description) {
        return new CreateDictionary()
                .setName(name)
                .setValues(new HashSet<>(FileUtils.parseValues(FileUtils.extractInputStream(file))))
                .setLanguage(locale)
                .setDescription(description);
    }
}
