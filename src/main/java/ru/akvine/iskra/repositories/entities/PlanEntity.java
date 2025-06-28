package ru.akvine.iskra.repositories.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.akvine.iskra.repositories.converters.RelationsMatrixToStringConverter;
import ru.akvine.iskra.repositories.dto.RelationsMatrix;
import ru.akvine.iskra.repositories.entities.base.BaseEntity;
import ru.akvine.iskra.repositories.entities.base.Identifiable;

@Setter
@Getter
@Accessors(chain = true)
@Table(name = "PLAN_ENTITY")
@Entity
public class PlanEntity extends BaseEntity<Long> implements Identifiable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planEntitySeq")
    @SequenceGenerator(name = "planEntitySeq", sequenceName = "SEQ_PLAN_ENTITY", allocationSize = 1000)
    @NotNull
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "UUID", nullable = false, updatable = false)
    private String uuid;

    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "LAST_PROCESS_UUID")
    @Nullable
    private String lastProcessUuid;

    @OneToOne
    @JoinColumn(name = "CONNECTION_ID", nullable = false)
    @NotNull
    private ConnectionEntity connection;

    @ManyToOne
    @NotNull
    @JoinColumn(name = "USER_ID", nullable = false)
    private UserEntity user;

    @Column(name = "RELATIONS_MATRIX_JSON")
    @Nullable
    @Convert(converter = RelationsMatrixToStringConverter.class)
    private RelationsMatrix relationsMatrix;
}
