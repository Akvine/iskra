package ru.akvine.iskra.rest.meta;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.dto.plan.CreatePlanRequest;
import ru.akvine.iskra.rest.dto.plan.GenerateDataRequest;

@RequestMapping(value = "/plans")
public interface PlanControllerMeta {
    @GetMapping(value = "/start/{uuid}")
    Response start(@PathVariable("uuid") String planUuid,
                   @RequestBody @Valid GenerateDataRequest request);

    @GetMapping
    Response list();

    @PostMapping
    Response create(@RequestBody @Valid CreatePlanRequest request);
}
