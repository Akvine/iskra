package ru.akvine.iskra.services.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.table.TableModel;

import java.util.Map;

@Data
@Accessors(chain = true)
//TODO: название класса очень схоже с GenerateDataAction. Поменять либо название этого класса, либо второго
public class GenerateData {
    private String userUuid;
    private PlanModel plan;
    private String processUuid;
    private Map<TableName, TableModel> selectedTables;
    private boolean resume;
}
