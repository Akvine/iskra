package ru.akvine.iskra.services.domain.table.process.dto;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.enums.ProcessState;

import java.util.Date;

@Data
@Accessors(chain = true)
public class UpdateTableProcess {
    private String processUuid;
    private String tableName;

    @Nullable
    private Long addSuccessRowsCount;
    @Nullable
    private String errorMessage;
    @Nullable
    private ProcessState state;
    @Nullable
    private Date completedDate;
}
