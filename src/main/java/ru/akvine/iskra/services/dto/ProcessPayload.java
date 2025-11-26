package ru.akvine.iskra.services.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.table.TableModel;

import java.util.Map;

@Data
@Accessors(chain = true)
public class ProcessPayload {
    private String processUuid;
    private PlanModel plan;
    private Map<TableName, TableModel> selectedTables;
    private boolean resume;
}
