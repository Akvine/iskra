package ru.akvine.iskra.repositories.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.iskra.enums.ProcessState;

import java.util.Date;

@Setter
@Getter
@Accessors(chain = true)
@Table(name = "TABLE_PROCESS_ENTITY")
@Entity
public class TableProcessEntity extends BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tableProcessEntitySeq")
    @SequenceGenerator(name = "tableProcessEntitySeq", sequenceName = "SEQ_TABLE_PROCESS_ENTITY", allocationSize = 1000)
    @NotNull
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "UUID", nullable = false, updatable = false)
    private String uuid = "STUB_UUID";

    @Column(name = "PID", nullable = false, updatable = false)
    private String pid;

    @Column(name = "TABLE_NAME", nullable = false)
    private String tableName;

    @Column(name = "SUCCESS_ROWS_COUNT")
    private long successRowsCount;

    @Column(name = "STATE", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProcessState processState;

    @Column(name = "STARTED_DATE")
    private Date startedDate;

    @Column(name = "COMPLETED_DATE")
    private Date completedDate;

    @Column(name = "ERROR_MESSAGE")
    private String errorMessage;

    @JoinColumn(name = "PLAN_ID", nullable = false)
    @NotNull
    @ManyToOne
    private PlanEntity plan;
}
