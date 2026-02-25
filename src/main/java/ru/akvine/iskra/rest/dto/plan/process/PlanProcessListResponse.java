package ru.akvine.iskra.rest.dto.plan.process;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;

@Data
@Accessors(chain = true)
public class PlanProcessListResponse extends SuccessfulResponse {
    private List<PlanProcessDto> processes;
}
