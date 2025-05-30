package ru.akvine.iskra.services.domain.table.configuration;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.compozit.commons.enums.DeleteMode;
import ru.akvine.iskra.repositories.entities.config.TableConfigurationEntity;
import ru.akvine.iskra.services.domain.base.Model;

@Data
@Accessors(chain = true)
public class TableConfigurationModel extends Model<Long> {
    private String name;
    private int rowsCount;
    private int batchSize;
    @Nullable
    private Long tableId;

    private boolean deleteDataBeforeStart;
    @Nullable
    private String clearScript;
    private DeleteMode deleteMode;

    public TableConfigurationModel(TableConfigurationEntity entity) {
        super(entity);

        this.name = entity.getName();
        this.rowsCount = entity.getRowsCount();
        this.batchSize = entity.getBatchSize();
        this.deleteDataBeforeStart = entity.isDeleteDataBeforeStart();
        this.clearScript = entity.getClearScript();
        this.deleteMode = entity.getDeleteMode();
        if (entity.getTable() != null) {
            this.tableId = entity.getTable().getId();
        }

    }
}
