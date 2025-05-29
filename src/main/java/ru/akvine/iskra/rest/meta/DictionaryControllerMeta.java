package ru.akvine.iskra.rest.meta;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.dto.dictionary.CreateDictionaryRequest;
import ru.akvine.iskra.rest.dto.dictionary.ListDictionariesRequest;

@RequestMapping(value = "/dictionaries")
public interface DictionaryControllerMeta {

    @GetMapping
    Response list(@RequestBody @Valid ListDictionariesRequest request);

    @PostMapping
    Response create(@RequestBody @Valid CreateDictionaryRequest request);

    @PostMapping(value = "/import")
    Response importFile(@RequestParam("file") MultipartFile file,
                        @RequestParam("name") String name,
                        @RequestParam(value = "lang", required = false) String lang,
                        @RequestParam(value = "description", required = false) String description);

}
