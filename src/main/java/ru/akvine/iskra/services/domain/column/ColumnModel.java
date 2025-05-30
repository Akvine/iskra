package ru.akvine.iskra.services.domain.column;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.enums.ConstraintType;
import ru.akvine.iskra.repositories.entities.ColumnEntity;
import ru.akvine.iskra.services.domain.base.Model;
import ru.akvine.iskra.services.domain.column.configuration.ColumnConfigurationModel;

import java.util.List;

@Data
@Accessors(chain = true)
public class ColumnModel extends Model<Long> {
    private String uuid;
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
    private String tableName;
    private List<ColumnConfigurationModel> configurations;
    private List<ConstraintType> constraints;

    public ColumnModel(ColumnEntity entity) {
        super(entity);

        this.uuid = entity.getUuid();
        this.columnName = entity.getColumnName();
        this.rawDataType = entity.getRawDataType();
        this.orderIndex = entity.getOrderIndex();
        this.size = entity.getSize();
        this.generatedAlways = entity.isGeneratedAlways();
        this.primaryKey = entity.isPrimaryKey();
        this.selected = entity.isSelected();
        this.constraints = entity.getConstraintTypes();
        this.configurations = entity.getConfigurations().stream()
                .map(ColumnConfigurationModel::new)
                .toList();

        this.tableName = entity.getTable().getName();
        this.database = entity.getDatabase();
        this.schemaName = entity.getSchemaName();
    }
}
