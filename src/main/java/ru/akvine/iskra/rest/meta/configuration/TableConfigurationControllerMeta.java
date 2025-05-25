package ru.akvine.iskra.rest.meta.configuration;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.dto.configuration.table.CreateTableConfigurationRequest;
import ru.akvine.iskra.rest.dto.configuration.table.UpdateTableConfigurationRequest;

@RequestMapping(value = "/tables/configurations")
public interface TableConfigurationControllerMeta {
    @PostMapping
    Response create(@RequestBody @Valid CreateTableConfigurationRequest request);

    @PatchMapping
    Response update(@RequestBody @Valid UpdateTableConfigurationRequest request);
}
