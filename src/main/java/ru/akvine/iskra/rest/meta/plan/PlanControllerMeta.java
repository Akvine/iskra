package ru.akvine.iskra.rest.meta.plan;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.dto.plan.CreatePlanRequest;
import ru.akvine.iskra.rest.dto.plan.DuplicatePlanRequest;
import ru.akvine.iskra.rest.dto.plan.PlanUpdateRequest;

@RequestMapping(value = "/plans")
public interface PlanControllerMeta {
    @GetMapping
    Response list();

    @PostMapping
    Response create(@RequestBody @Valid CreatePlanRequest request);

    @PostMapping(value = "/duplicate")
    Response duplicate(@RequestBody @Valid DuplicatePlanRequest request);

    @PatchMapping
    Response update(@RequestBody @Valid PlanUpdateRequest request);

    @DeleteMapping(value = "/{uuid}")
    Response delete(@PathVariable("uuid") String uuid);
}
