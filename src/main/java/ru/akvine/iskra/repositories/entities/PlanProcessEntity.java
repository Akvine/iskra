package ru.akvine.iskra.repositories.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.iskra.enums.ProcessState;
import ru.akvine.iskra.repositories.entities.base.Identifiable;
import ru.akvine.iskra.repositories.entities.base.SoftBaseEntity;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Table(name = "PLAN_PROCESS_ENTITY")
public class PlanProcessEntity extends SoftBaseEntity<Long> implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planProcessEntitySeq")
    @SequenceGenerator(name = "planProcessEntitySeq", sequenceName = "SEQ_PLAN_PROCESS_ENTITY", allocationSize = 1000)
    @NotNull
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "UUID", nullable = false, updatable = false)
    @NotNull
    private String uuid;

    @Column(name = "STATE", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProcessState processState = ProcessState.IN_PROGRESS;

    @Column(name = "TOTAL_TABLES_COUNT", nullable = false)
    private int totalTablesCount;

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
