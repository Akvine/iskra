package ru.akvine.iskra.rest.converter;

import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.rest.dto.plan.PlanDto;
import ru.akvine.iskra.rest.dto.plan.PlanListResponse;
import ru.akvine.iskra.services.domain.PlanModel;

import java.util.List;

@Component
public class PlanConverter {
    public PlanListResponse convertToProcessListResponse(List<PlanModel> processes) {
        Asserts.isNotNull(processes);
        return new PlanListResponse()
                .setProcesses(processes.stream().map(this::buildProcessDto).toList());
    }

    private PlanDto buildProcessDto(PlanModel process) {
        return new PlanDto().setUuid(process.getUuid());
    }
}
