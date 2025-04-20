package ru.akvine.iskra.services.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.Configuration;
import ru.akvine.compozit.commons.ConnectionDto;
import ru.akvine.compozit.commons.RelationsMatrixDto;

@Data
@Accessors(chain = true)
public class GenerateDataAction {
    private Configuration configuration;
    private RelationsMatrixDto relationsMatrix;
    private ConnectionDto connection;
}
