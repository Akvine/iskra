package ru.akvine.iskra.repositories.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.iskra.repositories.entities.base.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "TABLE_ENTITY")
@Entity
public class TableEntity extends BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tableEntitySeq")
    @SequenceGenerator(name = "tableEntitySeq", sequenceName = "SEQ_TABLE_ENTITY", allocationSize = 1000)
    @NotNull
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "UUID")
    private String uuid = "STUB_UUID";

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "SCHEMA")
    private String schema;

    @Column(name = "DATABASE")
    private String database;

    @OneToMany(mappedBy = "table", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ColumnEntity> columns = new ArrayList<>();

    @JoinColumn(name = "PLAN_ID", nullable = false)
    @ManyToOne
    @NotNull
    private PlanEntity plan;
}
