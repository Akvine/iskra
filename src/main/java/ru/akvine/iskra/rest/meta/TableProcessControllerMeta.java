package ru.akvine.iskra.rest.meta;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.akvine.compozit.commons.dto.Response;

@RequestMapping(value = "/table-processes")
public interface TableProcessControllerMeta {
    @GetMapping
    Response list(
            @RequestParam("uuid") String planUuid,
            @RequestParam(value = "processUuid", required = false) String processUuid);
}
