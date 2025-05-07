package ru.akvine.iskra.rest.meta;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.compozit.commons.GenerateDataRequest;
import ru.akvine.compozit.commons.dto.Response;

@RequestMapping(value = "/plans")
public interface PlanControllerMeta {
    @GetMapping(value = "/start/{uuid}")
    Response start(@PathVariable("uuid") String planUuid,
                   @RequestBody @Valid GenerateDataRequest request);

    @GetMapping
    Response list();
}
