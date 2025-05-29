package ru.akvine.iskra.rest.converter;

import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.rest.dto.dictionary.CreateDictionaryRequest;
import ru.akvine.iskra.rest.dto.dictionary.DictionaryDto;
import ru.akvine.iskra.rest.dto.dictionary.DictionaryListResponse;
import ru.akvine.iskra.rest.dto.dictionary.ListDictionariesRequest;
import ru.akvine.iskra.services.domain.DictionaryModel;
import ru.akvine.iskra.services.dto.dictionary.CreateDictionary;
import ru.akvine.iskra.services.dto.dictionary.ListDictionaries;
import ru.akvine.iskra.utils.StringHelper;

import java.util.List;

@Component
public class DictionaryConverter {
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
                .setName(request.getName())
                .setLanguage(request.getLanguage())
                .setDescription(request.getDescription())
                .setValues(request.getValues());
    }
}
