package ru.akvine.iskra.services.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.repositories.entities.PlanEntity;

@Data
@Accessors(chain = true)
public class PlanModel extends Model<Long> {
    public PlanModel(PlanEntity entity) {
        super(entity);
    }
}
