package ru.akvine.iskra.services.dto.process;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.enums.ProcessState;

import java.util.Date;

@Data
@Accessors(chain = true)
public class UpdateTableProcess {
    private String pid;
    private String processUuid;

    @Nullable
    private String errorMessage;
    @Nullable
    private ProcessState state;
    @Nullable
    private Date completedDate;
}
