package ru.akvine.iskra.services.dto.plan;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdatePlan {
    private String planUuid;

    @Nullable
    private String lastProcessUuid;
}
