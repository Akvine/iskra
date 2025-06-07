package ru.akvine.iskra.rest.dto.metadata;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;
import ru.akvine.iskra.repositories.dto.RelationsMatrix;

@Data
@Accessors(chain = true)
public class GenerateRelationsMatrixResponse extends SuccessfulResponse {
    private RelationsMatrix info;
}
