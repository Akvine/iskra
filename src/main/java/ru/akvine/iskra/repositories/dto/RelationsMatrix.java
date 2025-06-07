package ru.akvine.iskra.repositories.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.*;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public final class RelationsMatrix {
    private Map<String, Map<String, Boolean>> matrix;

    public RelationsMatrix(List<String> tableNames) {
        matrix = new HashMap<>();
        tableNames.forEach(tableNameColumn -> {
            Map<String, Boolean> columnValues = new HashMap<>();
            tableNames.forEach(tableNameRow -> columnValues.put(tableNameRow, Boolean.FALSE));
            matrix.put(tableNameColumn, columnValues);
        });
    }

    public Map<String, Boolean> getByColumn(String columName) {
        return matrix.get(columName);
    }

    public List<Boolean> getByRow(String rowName) {
        List<Boolean> values = new ArrayList<>();
        matrix.keySet().forEach(tableName -> values.add(matrix.get(tableName).get(rowName)));
        return values;
    }

    public boolean hasRelationsByColumn(String columnName) {
        Collection<Boolean> values = matrix.get(columnName).values();
        for (Boolean value : values) {
            if (Boolean.TRUE.equals(value)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasRelationsByRow(String rowName) {
        Set<String> columns = matrix.keySet();
        for (String columnName : columns) {
            boolean value = matrix.get(columnName).get(rowName);
            if (value) {
                return true;
            }
        }

        return false;
    }
}
