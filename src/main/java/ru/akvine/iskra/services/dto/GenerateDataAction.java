package ru.akvine.iskra.services.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.RelationsMatrixDto;

@Data
@Accessors(chain = true)
public class GenerateDataAction {
    private String planUuid;

    private RelationsMatrixDto relationsMatrix;
    private String connectionName;
}
