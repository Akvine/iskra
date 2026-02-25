package ru.akvine.iskra.repositories.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.iskra.repositories.entities.base.BaseEntity;
import ru.akvine.iskra.repositories.entities.config.TableConfigurationEntity;

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

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "IS_SELECTED", nullable = false)
    private boolean selected;

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

    @OneToOne
    @JoinColumn(name = "CONFIGURATION_ID")
    @Nullable
    private TableConfigurationEntity configuration;
}
