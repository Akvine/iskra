package ru.akvine.iskra.rest;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;
import ru.akvine.iskra.components.SecurityManager;
import ru.akvine.iskra.rest.mappers.DictionaryMapper;
import ru.akvine.iskra.rest.dto.dictionary.CreateDictionaryRequest;
import ru.akvine.iskra.rest.dto.dictionary.ListDictionariesRequest;
import ru.akvine.iskra.rest.meta.DictionaryControllerMeta;
import ru.akvine.iskra.rest.validators.DictionaryValidator;
import ru.akvine.iskra.services.domain.dictionary.DictionaryService;
import ru.akvine.iskra.services.domain.dictionary.DictionaryModel;
import ru.akvine.iskra.services.domain.dictionary.dto.CreateDictionary;
import ru.akvine.iskra.services.domain.dictionary.dto.ListDictionaries;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DictionaryController implements DictionaryControllerMeta {
    private final DictionaryValidator dictionaryValidator;
    private final DictionaryService dictionaryService;
    private final DictionaryMapper dictionaryMapper;

    private final SecurityManager securityManager;

    @Override
    public Response list(@RequestBody @Valid ListDictionariesRequest request) {
        ListDictionaries action = dictionaryMapper.convertToListDictionaries(request);
        List<DictionaryModel> dictionaries = dictionaryService.list(action);
        return dictionaryMapper.convertToDictionaryListResponse(dictionaries);
    }

    @Override
    public Response create(@RequestBody @Valid CreateDictionaryRequest request) {
        CreateDictionary createDictionaryAction = dictionaryMapper.convertToCreateDictionary(request);
        DictionaryModel createdDictionary = dictionaryService.create(createDictionaryAction);
        return dictionaryMapper.convertToDictionaryListResponse(List.of(createdDictionary));
    }

    @Override
    public Response delete(String uuid) {
        dictionaryService.delete(uuid, securityManager.getCurrentUser().getUuid());
        return new SuccessfulResponse();
    }

    @Override
    public Response importFile(MultipartFile file, String name, String lang, String description) {
        dictionaryValidator.verifyCreateDictionaryRequest(file);
        CreateDictionary createDictionaryAction = dictionaryMapper.convertToCreateDictionary(
                file, name, lang, description
        );
        DictionaryModel createdDictionary = dictionaryService.create(createDictionaryAction);
        return dictionaryMapper.convertToDictionaryListResponse(List.of(createdDictionary));
    }
}
