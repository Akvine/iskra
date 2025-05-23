package ru.akvine.iskra.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.converter.DictionaryConverter;
import ru.akvine.iskra.rest.dto.dictionary.CreateDictionaryRequest;
import ru.akvine.iskra.rest.dto.dictionary.ListDictionariesRequest;
import ru.akvine.iskra.rest.meta.DictionaryControllerMeta;
import ru.akvine.iskra.services.DictionaryService;
import ru.akvine.iskra.services.domain.DictionaryModel;
import ru.akvine.iskra.services.dto.dictionary.CreateDictionary;
import ru.akvine.iskra.services.dto.dictionary.ListDictionaries;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DictionaryController implements DictionaryControllerMeta {
    private final DictionaryService dictionaryService;
    private final DictionaryConverter dictionaryConverter;

    @Override
    public Response list(@RequestBody @Valid ListDictionariesRequest request) {
        ListDictionaries action = dictionaryConverter.convertToListDictionaries(request);
        List<DictionaryModel> dictionaries = dictionaryService.list(action);
        return dictionaryConverter.convertToDictionaryListResponse(dictionaries);
    }

    @Override
    public Response create(@RequestBody @Valid CreateDictionaryRequest request) {
        CreateDictionary createDictionaryAction = dictionaryConverter.convertToCreateDictionary(request);
        DictionaryModel createdDictionary = dictionaryService.create(createDictionaryAction);
        return dictionaryConverter.convertToDictionaryListResponse(List.of(createdDictionary));
    }
}
