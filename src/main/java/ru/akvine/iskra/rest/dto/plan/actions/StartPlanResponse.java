package ru.akvine.iskra.rest.dto.plan.actions;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;
import ru.akvine.iskra.enums.ProcessState;

@Data
@Accessors(chain = true)
public class StartPlanResponse extends SuccessfulResponse {
    private String processUuid;

    private final String state = ProcessState.IN_PROGRESS.toString();
}
