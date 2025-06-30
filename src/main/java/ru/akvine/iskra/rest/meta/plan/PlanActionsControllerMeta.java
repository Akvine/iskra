package ru.akvine.iskra.rest.meta.plan;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.dto.plan.actions.StartPlanRequest;

@RequestMapping(value = "/plans/actions")
public interface PlanActionsControllerMeta {
    @PostMapping(value = "/start")
    Response start(@RequestBody @Valid StartPlanRequest request);

    @PostMapping(value = "/stop/{uuid}")
    Response stop(@PathVariable("uuid") String planUuid);

    @PostMapping(value = "/scripts/generate/{uuid}")
    Response generateScripts(@PathVariable("uuid") String planUuid);
}
