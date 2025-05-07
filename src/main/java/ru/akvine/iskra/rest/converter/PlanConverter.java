package ru.akvine.iskra.rest.converter;

import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.GenerateDataRequest;
import ru.akvine.compozit.commons.TableConfig;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.rest.dto.plan.PlanDto;
import ru.akvine.iskra.rest.dto.plan.PlanListResponse;
import ru.akvine.iskra.services.domain.PlanModel;
import ru.akvine.iskra.services.dto.GenerateDataAction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PlanConverter {
    public PlanListResponse convertToProcessListResponse(List<PlanModel> processes) {
        Asserts.isNotNull(processes);
        return new PlanListResponse()
                .setProcesses(processes.stream().map(this::buildProcessDto).toList());
    }

    public GenerateDataAction convertToGenerateDataAction(String planUuid, GenerateDataRequest request) {
        return new GenerateDataAction()
                .setPlanUuid(planUuid)
                .setConnectionName(request.getConnectionName())
                .setRelationsMatrix(request.getRelationsMatrix())
                .setConfiguration(buildConfiguration(request.getConfiguration().getTablesConfigs()));
    }

    private Map<TableName, TableConfig> buildConfiguration(Map<String, TableConfig> tableConfigs) {
        Map<TableName, TableConfig> configs = new HashMap<>();
        tableConfigs.forEach((key, value) -> configs.put(new TableName(key), value));
        return configs;
    }

    private PlanDto buildProcessDto(PlanModel process) {
        return new PlanDto().setUuid(process.getUuid());
    }
}
