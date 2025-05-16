package ru.akvine.iskra.services.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.repositories.entities.PlanEntity;
import ru.akvine.iskra.services.domain.base.Model;

@Data
@Accessors(chain = true)
public class PlanModel extends Model<Long> {
    private String uuid;
    private String name;
    private ConnectionModel connection;

    public PlanModel(PlanEntity entity) {
        super(entity);

        this.uuid = entity.getUuid();
        this.name = entity.getName();
        this.connection = new ConnectionModel(entity.getConnection());
    }
}
