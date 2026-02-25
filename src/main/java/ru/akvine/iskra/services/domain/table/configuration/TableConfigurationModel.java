package ru.akvine.iskra.services.domain.table.configuration;

import jakarta.annotation.Nullable;
import java.util.List;
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

    @Nullable
    private String clearScript;

    private DeleteMode deleteMode;
    private boolean copyConfigurationForForeignKeys;

    private List<String> dropScripts;
    private List<String> createScripts;

    public TableConfigurationModel(TableConfigurationEntity entity) {
        super(entity);

        this.name = entity.getName();
        this.rowsCount = entity.getRowsCount();
        this.batchSize = entity.getBatchSize();
        this.clearScript = entity.getClearScripts();
        this.deleteMode = entity.getDeleteMode();
        this.dropScripts = entity.getDropScripts();
        this.createScripts = entity.getCreateScripts();
        if (entity.getTable() != null) {
            this.tableId = entity.getTable().getId();
        }
        this.copyConfigurationForForeignKeys = entity.isCopyConfigurationForForeignKeys();
    }
}
