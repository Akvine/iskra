package ru.akvine.iskra.repositories.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.iskra.enums.ConstraintType;
import ru.akvine.iskra.repositories.converters.ConstraintListToStringConverter;
import ru.akvine.iskra.repositories.entities.base.BaseEntity;
import ru.akvine.iskra.repositories.entities.base.Identifiable;
import ru.akvine.iskra.repositories.entities.config.ColumnConfigurationEntity;
import ru.akvine.iskra.repositories.entities.embaddable.ReferenceInfo;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "COLUMN_ENTITY")
@Entity
public class ColumnEntity extends BaseEntity<Long> implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "columnEntitySeq")
    @SequenceGenerator(name = "columnEntitySeq", sequenceName = "SEQ_COLUMN_ENTITY", allocationSize = 1000)
    @NotNull
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "UUID", nullable = false, updatable = false)
    @NotNull
    private String uuid;

    @Column(name = "COLUMN_NAME", nullable = false)
    private String columnName;

    @Column(name = "RAW_DATA_TYPE", nullable = false)
    private String rawDataType;

    @Column(name = "ORDER_INDEX", nullable = false)
    private int orderIndex;

    @Column(name = "SIZE", nullable = false)
    private int size;

    @Column(name = "IS_GENERATED_ALWAYS", nullable = false)
    private boolean generatedAlways;

    @Column(name = "PRIMARY_KEY", nullable = false)
    private boolean primaryKey;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private TableEntity table;

    @Column(name = "IS_SELECTED", nullable = false)
    private boolean selected = true;

    @Column(name = "CONSTRAINTS")
    @Convert(converter = ConstraintListToStringConverter.class)
    private List<ConstraintType> constraintTypes;

    @Embedded
    private ReferenceInfo referenceInfo;

    @Column(name = "CONFIGURATIONS", nullable = false)
    @OneToMany(mappedBy = "column", cascade = CascadeType.ALL, orphanRemoval = true)
    @NotNull
    private List<ColumnConfigurationEntity> configurations = new ArrayList<>();

    @Column(name = "DATABASE")
    @Nullable
    private String database;

    @Column(name = "SCHEMA")
    @Nullable
    private String schemaName;
}
