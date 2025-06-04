package ru.akvine.iskra.rest.dto.plan;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;

import java.util.Collection;

@Data
@Accessors(chain = true)
public class PlanListResponse extends SuccessfulResponse {
    private Collection<PlanDto> plans;
}
