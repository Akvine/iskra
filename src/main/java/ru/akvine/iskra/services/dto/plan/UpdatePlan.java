package ru.akvine.iskra.services.dto.plan;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.repositories.dto.RelationsMatrix;

@Data
@Accessors(chain = true)
public class UpdatePlan {
    private String planUuid;
    private String userUuid;

    @Nullable
    private String lastProcessUuid;
    @Nullable
    private RelationsMatrix relationsMatrix;
}
