package ru.akvine.iskra.rest.meta;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.compozit.commons.dto.Response;

@RequestMapping(value = "/processes")
public interface TableProcessControllerMeta {
    @GetMapping(value = "/{uuid}")
    Response list(@PathVariable("uuid") String planUuid);
}
