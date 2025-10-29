package ru.akvine.iskra.services.integration.visor.dto;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.enums.ConstraintType;

import java.util.List;

@Data
@Accessors(chain = true)
public class LoadConstraintsResult {
    private List<ConstraintType> constraintTypes;
    @Nullable
    private String targetColumnNameForForeignKey;
    @Nullable
    private String targetTableNameForForeignKey;
}
