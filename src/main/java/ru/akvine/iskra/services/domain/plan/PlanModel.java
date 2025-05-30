package ru.akvine.iskra.services.domain.plan;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.repositories.entities.PlanEntity;
import ru.akvine.iskra.services.domain.connection.ConnectionModel;
import ru.akvine.iskra.services.domain.base.Model;

@Data
@Accessors(chain = true)
public class PlanModel extends Model<Long> {
    private String uuid;
    @Nullable
    private String lastProcessUuid;
    private String name;
    private ConnectionModel connection;

    public PlanModel(PlanEntity entity) {
        super(entity);

        this.uuid = entity.getUuid();
        this.lastProcessUuid = entity.getLastProcessUuid();
        this.name = entity.getName();
        this.connection = new ConnectionModel(entity.getConnection());
    }
}
