package ru.akvine.iskra.services.domain.table.process;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.enums.ProcessState;
import ru.akvine.iskra.repositories.entities.TableProcessEntity;
import ru.akvine.iskra.services.domain.base.Model;
import ru.akvine.iskra.services.domain.process.PlanProcessModel;

import java.util.Date;

@Data
@Accessors(chain = true)
public class TableProcessModel extends Model<Long> {
    private String tableName;
    private long successRowsCount;
    private long totalRowsCount;
    private ProcessState processState;
    private Date startedDate;
    private Date completedDate;
    private String errorMessage;
    private PlanProcessModel planProcess;
    private int successRowsPercent;

    public TableProcessModel(TableProcessEntity entity) {
        super(entity);
        this.tableName = entity.getTableName();
        this.successRowsCount = entity.getSuccessRowsCount();
        this.totalRowsCount = entity.getTotalRowsCount();
        this.processState = entity.getProcessState();
        this.startedDate = entity.getStartedDate();
        this.completedDate = entity.getCompletedDate();
        this.errorMessage = entity.getErrorMessage();
        this.planProcess = new PlanProcessModel(entity.getProcess());

        if (totalRowsCount != 0) {
            successRowsPercent = (int) (successRowsCount / totalRowsCount * 100);
        }
    }
}
