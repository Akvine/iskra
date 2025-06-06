package ru.akvine.iskra.repositories.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.iskra.enums.Language;
import ru.akvine.iskra.repositories.entities.base.BaseEntity;
import ru.akvine.iskra.repositories.entities.base.Identifiable;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "DICTIONARY_ENTITY")
@Entity
public class DictionaryEntity extends BaseEntity<Long> implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dictionaryEntitySeq")
    @SequenceGenerator(name = "dictionaryEntitySeq", sequenceName = "SEQ_DICTIONARY_ENTITY", allocationSize = 1000)
    @NotNull
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "UUID", nullable = false, updatable = false)
    @NotNull
    private String uuid;

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

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @Nullable
    private UserEntity user;
}
