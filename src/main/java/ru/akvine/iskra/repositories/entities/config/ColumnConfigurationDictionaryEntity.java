package ru.akvine.iskra.repositories.entities.config;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.iskra.repositories.entities.DictionaryEntity;
import ru.akvine.iskra.repositories.entities.base.SoftBaseEntity;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Entity
@Table(name = "COLUMN_CONFIGURATION_DICTIONARY_ENTITY")
public class ColumnConfigurationDictionaryEntity extends SoftBaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "columnConfigurationDictionaryEntitySeq")
    @SequenceGenerator(name = "columnConfigurationDictionaryEntitySeq",
            sequenceName = "SEQ_COLUMN_CONFIGURATION_DICTIONARY_ENTITY", allocationSize = 1000)
    @NotNull
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COLUMN_CONFIGURATION_ID", nullable = false)
    private ColumnConfigurationEntity columnConfiguration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DICTIONARY_ID", nullable = false)
    private DictionaryEntity dictionary;
}
