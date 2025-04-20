package ru.akvine.iskra.rest.meta;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.compozit.commons.GenerateDataRequest;

@RequestMapping(value = "/generate")
public interface GenerateControllerMeta {

    @GetMapping(value = "/start")
    ResponseEntity<?> start(@RequestBody @Valid GenerateDataRequest request);
}
