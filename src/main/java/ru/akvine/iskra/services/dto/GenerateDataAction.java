package ru.akvine.iskra.services.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.RelationsMatrixDto;
import ru.akvine.compozit.commons.TableConfig;
import ru.akvine.compozit.commons.TableName;

import java.util.Map;

@Data
@Accessors(chain = true)
public class GenerateDataAction {
    private String planUuid;

    private Map<TableName, TableConfig> configuration;
    private RelationsMatrixDto relationsMatrix;
    private String connectionName;
}
