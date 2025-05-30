package ru.akvine.iskra.services.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.services.domain.table.TableModel;

@Data
@Accessors(chain = true)
public class GenerateDataAction {
    private String processUuid;
    private TableModel table;
    private boolean resume;
}
