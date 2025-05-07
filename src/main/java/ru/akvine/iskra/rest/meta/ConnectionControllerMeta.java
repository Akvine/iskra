package ru.akvine.iskra.rest.meta;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.dto.connection.CreateConnectionRequest;

@RequestMapping(value = "/connections")
public interface ConnectionControllerMeta {
    @GetMapping
    Response list();

    @PostMapping
    Response create(@RequestBody @Valid CreateConnectionRequest request);
}
