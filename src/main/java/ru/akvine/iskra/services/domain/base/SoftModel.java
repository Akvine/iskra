package ru.akvine.iskra.services.domain.base;


import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.akvine.iskra.repositories.entities.base.SoftBaseEntity;

import java.util.Date;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public abstract class SoftModel<ID> extends Model<ID> {
    @Nullable
    protected Date deletedDate;
    protected boolean deleted;

    public SoftModel(SoftBaseEntity<ID> softBaseEntity) {
        super(softBaseEntity);

        this.deleted = softBaseEntity.isDeleted();
        this.deletedDate = softBaseEntity.getDeletedDate();
    }
}
