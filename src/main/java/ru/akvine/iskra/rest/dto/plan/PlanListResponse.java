package ru.akvine.iskra.rest.dto.plan;

import java.util.Collection;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;

@Data
@Accessors(chain = true)
public class PlanListResponse extends SuccessfulResponse {
    private Collection<PlanDto> plans;
}
