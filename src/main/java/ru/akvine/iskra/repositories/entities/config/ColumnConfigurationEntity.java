package ru.akvine.iskra.repositories.entities.config;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.iskra.repositories.entities.ColumnEntity;
import ru.akvine.iskra.repositories.entities.DictionaryEntity;
import ru.akvine.iskra.repositories.entities.base.BaseEntity;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "COLUMN_CONFIGURATION_ENTITY")
@Entity
public class ColumnConfigurationEntity extends BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "columnConfigurationEntitySeq")
    @SequenceGenerator(name = "columnConfigurationEntitySeq", sequenceName = "SEQ_COLUMN_CONFIGURATION_ENTITY", allocationSize = 1000)
    @NotNull
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "NAME", nullable = false)
    @NotNull
    private String name;

    @Column(name = "SELECTED", nullable = false)
    private boolean selected;

    @Column(name = "TYPE", nullable = false)
    private String type;

    @Column(name = "GENERATION_STRATEGY", nullable = false)
    private String generationStrategy;

    @Column(name = "IS_UNIQUE", nullable = false)
    private boolean unique;

    @Column(name = "IS_NOT_NULL", nullable = false)
    private boolean notNull;

    @Column(name = "RANGE_TYPE")
    private String rangeType;

    @Column(name = "START")
    private String start;

    @Column(name = "END_VAR")
    private String end;

    @Column(name = "STEP")
    private String step;

    @Column(name = "IS_VALID")
    private Boolean valid;

    @Column(name = "REGEXPS")
    private String regexps;

    @OneToOne
    @JoinColumn(name = "DICTIONARY_ID")
    @Nullable
    private DictionaryEntity dictionary;

    @ManyToOne
    @JoinColumn(name = "COLUMN_ID", nullable = false)
    @NotNull
    private ColumnEntity column;
}
