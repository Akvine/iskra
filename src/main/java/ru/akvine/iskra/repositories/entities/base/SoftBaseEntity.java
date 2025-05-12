package ru.akvine.iskra.repositories.entities.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.lang.Nullable;

import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@MappedSuperclass
public abstract class SoftBaseEntity<ID> extends BaseEntity<ID> {
    @Column(name = "DELETED_DATE")
    @Nullable
    private Date deletedDate;

    @Column(name = "IS_DELETED", nullable = false)
    private boolean deleted;
}
