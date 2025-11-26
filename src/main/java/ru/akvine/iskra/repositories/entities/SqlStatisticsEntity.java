package ru.akvine.iskra.repositories.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.iskra.enums.ProcessState;
import ru.akvine.iskra.enums.SqlScriptType;
import ru.akvine.iskra.repositories.entities.base.Identifiable;
import ru.akvine.iskra.repositories.entities.base.SoftBaseEntity;

import java.util.Date;

@Setter
@Getter
@Accessors(chain = true)
@Table(name = "SQL_STATISTICS_ENTITY")
@Entity
public class SqlStatisticsEntity extends SoftBaseEntity<Long> implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sqlStatisticsEntitySeq")
    @SequenceGenerator(name = "sqlStatisticsEntitySeq", sequenceName = "SEQ_SQL_STATISTICS_ENTITY", allocationSize = 1000)
    @NotNull
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "UUID", nullable = false, updatable = false)
    private String uuid;

    @Column(name = "PROCESS_UUID", nullable = false)
    private String processUuid;

    @Column(name = "TABLE_NAME", nullable = false)
    private String tableName;

    @Column(name = "TABLE_ID", nullable = false)
    private Long tableId;

    @Column(name = "SQL_SCRIPT_TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    private SqlScriptType sqlScriptType;

    @Column(name = "PROCESS_STATE", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProcessState processState = ProcessState.IN_PROGRESS;

    @Column(name = "PROCESS_DURATION")
    private String processDuration;

    @Column(name = "START_PROCESS_DATE", nullable = false)
    private Date startProcessDate;

    @Column(name = "END_PROCESS_DATE")
    private Date endProcessDate;

    @Column(name = "ERROR_MESSAGE")
    @Lob
    private String errorMessage;
}
