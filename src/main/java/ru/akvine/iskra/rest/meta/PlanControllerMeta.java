package ru.akvine.iskra.rest.meta;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.compozit.commons.dto.Response;

@RequestMapping(value = "/plans")
public interface PlanControllerMeta {
    @GetMapping
    Response list();
}
