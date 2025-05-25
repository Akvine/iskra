package ru.akvine.iskra.repositories.entities.config;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import ru.akvine.compozit.commons.enums.DeleteMode;
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

    @Column(name = "DELETE_DATA_BEFORE_START", nullable = false)
    private boolean deleteDataBeforeStart;

    @Column(name = "DELETE_MODE")
    @Enumerated(EnumType.STRING)
    private DeleteMode deleteMode;

    @Column(name = "CLEAR_SCRIPT")
    @Nullable
    private String clearScript = StringUtils.EMPTY;

    @OneToOne
    @JoinColumn(name = "TABLE_ID")
    @Nullable
    private TableEntity table;
}
