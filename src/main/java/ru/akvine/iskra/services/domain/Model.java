package ru.akvine.iskra.services.domain;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import ru.akvine.iskra.repositories.entities.BaseEntity;

import java.util.Date;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public abstract class Model<ID> {
    protected ID id;
    protected String uuid;
    protected Date createdDate;
    @Nullable
    protected Date updatedDate;
    @Nullable
    protected Date deletedDate;
    protected boolean deleted;

    public Model(BaseEntity<ID> baseEntity) {
        this.uuid = baseEntity.getUuid();
        this.createdDate = baseEntity.getCreatedDate();
        this.updatedDate = baseEntity.getUpdatedDate();
        this.deletedDate = baseEntity.getDeletedDate();
        this.deleted = baseEntity.isDeleted();
    }
}
