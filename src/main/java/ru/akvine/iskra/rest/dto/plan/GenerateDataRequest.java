package ru.akvine.iskra.rest.dto.plan;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.Configuration;
import ru.akvine.compozit.commons.RelationsMatrixDto;

@Data
@Accessors(chain = true)
public class GenerateDataRequest {
    private @NotNull Configuration configuration;
    private @NotNull RelationsMatrixDto relationsMatrix;
}
