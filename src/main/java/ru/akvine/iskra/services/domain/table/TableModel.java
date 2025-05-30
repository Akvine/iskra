package ru.akvine.iskra.services.domain.table;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.repositories.entities.TableEntity;
import ru.akvine.iskra.services.domain.column.ColumnModel;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.base.Model;
import ru.akvine.iskra.services.domain.table.configuration.TableConfigurationModel;

import java.util.List;

@Data
@Accessors(chain = false)
public class TableModel extends Model<Long> {
    private String tableName;
    @Nullable
    private String schema;
    @Nullable
    private String database;
    private PlanModel plan;
    private List<ColumnModel> columns;
    private boolean selected;
    private TableConfigurationModel configuration;

    public TableModel(TableEntity table) {
        super(table);

        this.tableName = table.getName();
        this.schema = table.getSchema();
        this.database = table.getDatabase();
        this.plan = new PlanModel(table.getPlan());
        this.selected = table.isSelected();
        this.columns = table.getColumns().stream()
                .map(ColumnModel::new)
                .toList();

        if (table.getConfiguration() != null) {
            this.configuration = new TableConfigurationModel(table.getConfiguration());
        }
    }
}
