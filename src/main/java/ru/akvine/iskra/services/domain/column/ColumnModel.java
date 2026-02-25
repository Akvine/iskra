package ru.akvine.iskra.services.domain.column;

import jakarta.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.CollectionUtils;
import ru.akvine.iskra.enums.ConstraintType;
import ru.akvine.iskra.enums.RelationShipType;
import ru.akvine.iskra.repositories.entities.ColumnEntity;
import ru.akvine.iskra.services.domain.base.Model;
import ru.akvine.iskra.services.domain.column.configuration.ColumnConfigurationModel;

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

    @Nullable
    private String targetColumnNameForForeignKey;

    @Nullable
    private String targetTableNameForForeignKey;

    @Nullable
    private RelationShipType relationShipType;

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
        this.configurations = entity.getConfigurations().stream()
                .map(ColumnConfigurationModel::new)
                .toList();

        this.tableName = entity.getTable().getName();
        this.database = entity.getDatabase();
        this.schemaName = entity.getSchemaName();

        if (CollectionUtils.isEmpty(entity.getConstraintTypes())) {
            this.constraints = new ArrayList<>();
        } else {
            this.constraints = entity.getConstraintTypes();
        }

        if (entity.getReferenceInfo() != null) {
            this.relationShipType = entity.getReferenceInfo().getRelationShipType();
            this.targetColumnNameForForeignKey = entity.getReferenceInfo().getTargetColumnNameForForeignKey();
            this.targetTableNameForForeignKey = entity.getReferenceInfo().getTargetTableNameForForeignKey();
        }
    }
}
