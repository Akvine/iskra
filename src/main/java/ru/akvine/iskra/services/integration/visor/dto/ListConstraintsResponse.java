package ru.akvine.iskra.services.integration.visor.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;
import ru.akvine.iskra.enums.ConstraintType;

import java.util.List;

@Data
@Accessors(chain = true)
public class ListConstraintsResponse extends SuccessfulResponse {
    private List<ConstraintType> constraintTypes;
}