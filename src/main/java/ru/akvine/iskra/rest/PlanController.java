package ru.akvine.iskra.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.converter.PlanConverter;
import ru.akvine.iskra.rest.meta.PlanControllerMeta;
import ru.akvine.iskra.services.PlanService;
import ru.akvine.iskra.services.domain.PlanModel;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class PlanController implements PlanControllerMeta {
    private final PlanService planService;
    private final PlanConverter planConverter;

    @Override
    public Response list() {
        List<PlanModel> processes = planService.list();
        return planConverter.convertToProcessListResponse(processes);
    }
}
