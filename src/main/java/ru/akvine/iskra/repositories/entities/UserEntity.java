package ru.akvine.iskra.repositories.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.akvine.iskra.repositories.entities.base.Identifiable;
import ru.akvine.iskra.repositories.entities.base.SoftBaseEntity;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
@Table(name = "USER_ENTITY")
@Entity
@NoArgsConstructor
public class UserEntity extends SoftBaseEntity<Long> implements UserDetails, Identifiable {
    @Id
    @Column(name = "ID", nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userEntitySeq")
    @SequenceGenerator(name = "userEntitySeq", sequenceName = "SEQ_USER_ENTITY", allocationSize = 1000)
    @NotNull
    private Long id;

    @Column(name = "UUID", nullable = false, updatable = false)
    @NotNull
    private String uuid;

    @Column(name = "USERNAME", nullable = false)
    @NotNull
    private String username;

    @Column(name = "EMAIL", nullable = false)
    @NotNull
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }
}
