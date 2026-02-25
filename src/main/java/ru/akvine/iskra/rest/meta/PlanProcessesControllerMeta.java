package ru.akvine.iskra.rest.meta;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.akvine.compozit.commons.dto.Response;

@RequestMapping(value = "/processes")
public interface PlanProcessesControllerMeta {
    @GetMapping
    Response list(@RequestParam("processUuid") String processUuid);
}
