package ru.akvine.iskra.repositories.entities.config;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.iskra.repositories.entities.TableEntity;
import ru.akvine.iskra.repositories.entities.base.BaseEntity;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "TABLE_CONFIGURATION_ENTITY")
@Entity
@NoArgsConstructor
public class TableConfigurationEntity extends BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tableConfigurationEntitySeq")
    @SequenceGenerator(name = "tableConfigurationEntitySeq", sequenceName = "SEQ_TABLE_CONFIGURATION_ENTITY", allocationSize = 1000)
    @NotNull
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "NAME", nullable = false)
    @NotNull
    private String name;

    @Column(name = "ROWS_COUNT", nullable = false)
    private int rowsCount;

    @Column(name = "BATCH_SIZE", nullable = false)
    private int batchSize;

    @OneToOne
    @JoinColumn(name = "TABLE_ID")
    @Nullable
    private TableEntity table;
}
