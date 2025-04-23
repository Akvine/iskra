package ru.akvine.iskra.services.integration.visor;

import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.*;
import ru.akvine.compozit.commons.iskra.InsertValuesRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class VisorDtoConverter {
    public InsertValuesRequest convert(TableName tableName,
                                       byte[] table,
                                       TableConfig config,
                                       ConnectionDto connection) {
        return new InsertValuesRequest()
                .setTableName(tableName.getName())
                .setContent(table)
                .setConnection(connection)
                .setColumnsMetaInfo(buildColumnsMetaInfo(config.getColumnConfigs()));

    }

    private Map<String, ColumnMetaInfoDto> buildColumnsMetaInfo(List<ColumnInfo> columnConfigs) {
        Map<String, ColumnMetaInfoDto> metaInfo = new HashMap<>();
        columnConfigs.forEach(config -> metaInfo.put(config
                .getColumn()
                .getName(), config.getColumnMetaInfo()));
        return metaInfo;
    }
}
