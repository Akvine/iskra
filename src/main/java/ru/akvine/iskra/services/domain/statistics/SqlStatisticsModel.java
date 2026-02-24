package ru.akvine.iskra.services.domain.statistics;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.enums.ProcessState;
import ru.akvine.iskra.enums.SqlScriptType;
import ru.akvine.iskra.repositories.entities.SqlStatisticsEntity;
import ru.akvine.iskra.services.domain.base.SoftModel;

import java.util.Date;

@Data
@Accessors(chain = true)
public class SqlStatisticsModel extends SoftModel<Long> {
    private String processUuid;

    private String tableName;
    private Long tableId;

    private SqlScriptType sqlScriptType;
    private ProcessState processState;
    private String processDuration;
    private Date startProcessDate;
    private Date endProcessDate;
    private String errorMessage;

    public SqlStatisticsModel(SqlStatisticsEntity entity) {
        super(entity);
        this.uuid = entity.getUuid();

        this.processUuid = entity.getProcessUuid();
        this.tableId = entity.getTableId();
        this.tableName = entity.getTableName();

        this.sqlScriptType = entity.getSqlScriptType();
        this.processState = entity.getProcessState();
        this.processDuration = entity.getProcessDuration();
        this.startProcessDate = entity.getStartProcessDate();
        this.endProcessDate = entity.getEndProcessDate();
    }
}
