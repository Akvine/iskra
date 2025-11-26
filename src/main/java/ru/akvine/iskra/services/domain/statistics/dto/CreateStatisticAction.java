package ru.akvine.iskra.services.domain.statistics.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.enums.SqlScriptType;

@Data
@Accessors(chain = true)
public class CreateStatisticAction {
    private String processUuid;
    private SqlScriptType scriptType;

    private String tableName;
    private long tableId;
}
