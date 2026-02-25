package ru.akvine.iskra.rest.mappers;

import org.springframework.stereotype.Component;
import ru.akvine.iskra.rest.dto.plan.process.PlanProcessDto;
import ru.akvine.iskra.rest.dto.plan.process.PlanProcessListResponse;
import ru.akvine.iskra.services.domain.process.PlanProcessModel;

import java.util.List;

@Component
public class PlanProcessMapper {
    public PlanProcessListResponse mapToPlanProcessListResponse(List<PlanProcessModel> planProcesses) {
        return new PlanProcessListResponse()
                .setProcesses(planProcesses.stream()
                        .map(this::buildPlanProcessDto)
                        .toList());
    }

    private PlanProcessDto buildPlanProcessDto(PlanProcessModel planProcessModel) {
        return new PlanProcessDto()
                .setUuid(planProcessModel.getUuid())
                .setErrorMessage(planProcessModel.getErrorMessage())
                .setState(planProcessModel.getState().toString())
                .setCompletedDate(planProcessModel.getCompletedDate())
                .setStartedDate(planProcessModel.getStartedDate())
                .setTotalTablesCount(planProcessModel.getTotalTablesCount());
    }
}
