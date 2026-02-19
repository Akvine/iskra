package ru.akvine.iskra.services.domain.process;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.enums.ProcessState;
import ru.akvine.iskra.repositories.entities.PlanProcessEntity;
import ru.akvine.iskra.services.domain.base.SoftModel;

import java.util.Date;

@Data
@Accessors(chain = true)
public class PlanProcessModel extends SoftModel<Long> {
    private ProcessState state;
    private int totalTablesCount;
    private Date startedDate;
    private Date completedDate;
    private String errorMessage;

    public PlanProcessModel(PlanProcessEntity entity) {
        super(entity);

        this.state = entity.getProcessState();
        this.totalTablesCount = entity.getTotalTablesCount();

        this.startedDate = entity.getStartedDate();
        this.completedDate = entity.getStartedDate();
        this.errorMessage = entity.getErrorMessage();
    }
}
