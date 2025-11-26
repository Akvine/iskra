package ru.akvine.iskra.services.domain.statistics.dto;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.enums.ProcessState;

@Data
@Accessors(chain = true)
public class UpdateStatisticAction {
    private String uuid;

    @Nullable
    private ProcessState state;

}
