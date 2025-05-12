package ru.akvine.iskra.repositories.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.iskra.repositories.entities.base.BaseEntity;

@Setter
@Getter
@Accessors(chain = true)
@Table(name = "PLAN_ENTITY")
@Entity
public class PlanEntity extends BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planEntitySeq")
    @SequenceGenerator(name = "planEntitySeq", sequenceName = "SEQ_PLAN_ENTITY", allocationSize = 1000)
    @NotNull
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "UUID", nullable = false, updatable = false)
    private String uuid;

    @Column(name = "NAME", nullable = false)
    private String name;

    @OneToOne
    @JoinColumn(name = "CONNECTION_ID", nullable = false)
    @NotNull
    private ConnectionEntity connection;
}
