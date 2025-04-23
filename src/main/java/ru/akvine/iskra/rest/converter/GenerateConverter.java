package ru.akvine.iskra.rest.converter;

import org.springframework.stereotype.Component;
import ru.akvine.compozit.commons.GenerateDataRequest;
import ru.akvine.compozit.commons.TableConfig;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.services.dto.GenerateDataAction;

import java.util.HashMap;
import java.util.Map;

@Component
public class GenerateConverter {
    public GenerateDataAction convertToGenerateDataAction(GenerateDataRequest request) {
        return new GenerateDataAction()
                .setConnection(request.getConnection())
                .setRelationsMatrix(request.getRelationsMatrix())
                .setConfiguration(buildConfiguration(request.getConfiguration().getTablesConfigs()));
    }

    private Map<TableName, TableConfig> buildConfiguration(Map<String, TableConfig> tableConfigs) {
        Map<TableName, TableConfig> configs = new HashMap<>();
        tableConfigs.forEach((key, value) -> configs.put(new TableName(key), value));
        return configs;
    }
}
