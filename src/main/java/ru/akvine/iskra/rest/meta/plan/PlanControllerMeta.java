package ru.akvine.iskra.rest.meta.plan;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.dto.plan.CreatePlanRequest;
import ru.akvine.iskra.rest.dto.plan.DuplicatePlanRequest;

@RequestMapping(value = "/plans")
public interface PlanControllerMeta {
    @GetMapping
    Response list();

    @PostMapping
    Response create(@RequestBody @Valid CreatePlanRequest request);

    @PostMapping(value = "/duplicate")
    Response duplicate(@RequestBody @Valid DuplicatePlanRequest request);
}
