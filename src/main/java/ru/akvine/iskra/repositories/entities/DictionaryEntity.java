package ru.akvine.iskra.repositories.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.iskra.enums.Language;
import ru.akvine.iskra.repositories.entities.base.BaseEntity;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "DICTIONARY_ENTITY")
@Entity
public class DictionaryEntity extends BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dictionaryEntitySeq")
    @SequenceGenerator(name = "dictionaryEntitySeq", sequenceName = "SEQ_DICTIONARY_ENTITY", allocationSize = 1000)
    @NotNull
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "LANGUAGE", nullable = false)
    @Enumerated(EnumType.STRING)
    private Language language = Language.RU;

    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "IS_SYSTEM", nullable = false)
    private boolean system;

    @Column(name = "VALUES", nullable = false)
    private String values;
}
