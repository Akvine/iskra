package ru.akvine.iskra.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.rest.mappers.PlanProcessMapper;
import ru.akvine.iskra.rest.meta.PlanProcessesControllerMeta;
import ru.akvine.iskra.services.domain.process.PlanProcessModel;
import ru.akvine.iskra.services.domain.process.PlanProcessService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PlanProcessesController implements PlanProcessesControllerMeta {
    private final PlanProcessService planProcessService;
    private final PlanProcessMapper planProcessMapper;

    @Override
    public Response list(String processUuid) {
        PlanProcessModel planProcess = planProcessService.get(processUuid);
        return planProcessMapper.mapToPlanProcessListResponse(List.of(planProcess));
    }
}
