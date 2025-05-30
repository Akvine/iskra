package ru.akvine.iskra.rest.meta.plan;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.compozit.commons.dto.Response;

@RequestMapping(value = "/plans/actions")
public interface PlanActionsControllerMeta {
    @PostMapping(value = "/start/{uuid}")
    Response start(@PathVariable("uuid") String planUuid);

    @PostMapping(value = "/stop/{uuid}")
    Response stop(@PathVariable("uuid") String planUuid);

    @PostMapping(value = "/resume/{uuid}")
    Response resume(@PathVariable("uuid") String planUuid);
}
