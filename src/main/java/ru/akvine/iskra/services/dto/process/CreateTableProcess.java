package ru.akvine.iskra.services.dto.process;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.TableConfig;
import ru.akvine.compozit.commons.TableName;

@Data
@Accessors(chain = true)
public class CreateTableProcess {
    private String processUuid;
    private TableName tableName;
    private TableConfig config;
}
