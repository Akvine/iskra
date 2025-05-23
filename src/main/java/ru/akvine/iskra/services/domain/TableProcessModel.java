package ru.akvine.iskra.services.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.enums.ProcessState;
import ru.akvine.iskra.repositories.entities.TableProcessEntity;
import ru.akvine.iskra.services.domain.base.Model;

import java.util.Date;

@Data
@Accessors(chain = true)
public class TableProcessModel extends Model<Long> {
    private String pid;
    private String processUuid;
    private String tableName;
    private long successRowsCount;
    private ProcessState processState;
    private Date startedDate;
    private Date completedDate;
    private String errorMessage;
    private PlanModel plan;

    public TableProcessModel(TableProcessEntity entity) {
        super(entity);
        this.pid = entity.getPid();
        this.processUuid = entity.getProcessUuid();
        this.tableName = entity.getTableName();
        this.successRowsCount = entity.getSuccessRowsCount();
        this.processState = entity.getProcessState();
        this.startedDate = entity.getStartedDate();
        this.completedDate = entity.getCompletedDate();
        this.errorMessage = entity.getErrorMessage();
        this.plan = new PlanModel(entity.getPlan());
    }
}
