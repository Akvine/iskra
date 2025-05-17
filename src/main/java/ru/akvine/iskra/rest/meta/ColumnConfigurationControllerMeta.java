package ru.akvine.iskra.rest.meta;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.dto.configuration.CreateConfigurationRequest;
import ru.akvine.iskra.rest.dto.configuration.SelectConfigurationRequest;

@RequestMapping(value = "/configurations")
public interface ColumnConfigurationControllerMeta {

    @GetMapping("/{columnUuid}")
    Response list(@PathVariable("columnUuid") String columnUuid);

    @PostMapping
    Response create(@RequestBody @Valid CreateConfigurationRequest request);

    @PatchMapping("/select")
    Response select(@RequestBody @Valid SelectConfigurationRequest request);
}
