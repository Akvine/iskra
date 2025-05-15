package ru.akvine.iskra.services.domain;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.repositories.entities.ColumnEntity;
import ru.akvine.iskra.services.domain.base.Model;

@Data
@Accessors(chain = true)
public class ColumnModel extends Model<Long> {
    private String columnName;
    private String rawDataType;
    private int orderIndex;
    private int size;
    private boolean selected;
    private boolean generatedAlways;
    private boolean primaryKey;
    @Nullable
    private String database;
    @Nullable
    private String schemaName;

    public ColumnModel(ColumnEntity entity) {
        super(entity);

        this.columnName = entity.getColumnName();
        this.rawDataType = entity.getRawDataType();
        this.orderIndex = entity.getOrderIndex();
        this.size = entity.getSize();
        this.generatedAlways = entity.isGeneratedAlways();
        this.primaryKey = entity.isPrimaryKey();
        this.selected = entity.isSelected();

        this.database = entity.getDatabase();
        this.schemaName = entity.getSchemaName();
    }
}
