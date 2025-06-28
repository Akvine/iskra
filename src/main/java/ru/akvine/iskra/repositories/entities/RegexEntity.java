package ru.akvine.iskra.repositories.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.iskra.repositories.entities.base.Identifiable;
import ru.akvine.iskra.repositories.entities.base.SoftBaseEntity;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@Table(name = "REGEX_ENTITY")
@Entity
public class RegexEntity extends SoftBaseEntity<Long> implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "regexEntitySeq")
    @SequenceGenerator(name = "regexEntitySeq", sequenceName = "SEQ_REGEX_ENTITY", allocationSize = 1000)
    @NotNull
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "UUID", nullable = false, updatable = false)
    @NotNull
    private String uuid;

    @Column(name = "NAME", nullable = false)
    @NotNull
    private String name;

    @Column(name = "PATTERN", nullable = false)
    @NotNull
    private String pattern;

    @Column(name = "IS_SYSTEM", nullable = false)
    private boolean system;

    @Column(name = "DESCRIPTION")
    @Nullable
    private String description;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private UserEntity user;
}
