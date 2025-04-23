package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.akvine.compozit.commons.ConnectionDto;
import ru.akvine.compozit.commons.RelationsMatrixDto;
import ru.akvine.compozit.commons.TableConfig;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.services.GenerateDataFacade;
import ru.akvine.iskra.services.TableProcessService;
import ru.akvine.iskra.services.domain.TableProcessModel;
import ru.akvine.iskra.services.dto.GenerateDataAction;
import ru.akvine.iskra.services.integration.istochnik.IstochnikService;
import ru.akvine.iskra.services.integration.visor.VisorService;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GenerateDataFacadeImpl implements GenerateDataFacade {
    private final VisorService visorService;
    private final IstochnikService istochnikService;
    private final TableProcessService tableProcessService;

    @Override
    public void generateData(GenerateDataAction action) {
        RelationsMatrixDto relationsMatrix = action.getRelationsMatrix();
        Map<TableName, TableConfig> configuration = action.getConfiguration();
        ConnectionDto connection = action.getConnection();

        if (CollectionUtils.isEmpty(configuration)) {
            return;
        }

        List<TableName> tableNamesHasNoRelations = relationsMatrix.getRows().stream()
                .filter(row -> !row.hasRelations())
                .map(row -> new TableName(row.getTableName()))
                .toList();

        for (TableName tableName : tableNamesHasNoRelations) {
            TableConfig config = configuration.get(tableName);
            generateData(tableName, config, connection);
        }
    }

    @Override
    public TableProcessModel generateData(TableName tableName, TableConfig config, ConnectionDto connection) {
        TableProcessModel tableProcess = tableProcessService.create(tableName, config);
        generateDataInternal(tableName, config, connection);
        return tableProcess;
    }

    private void generateDataInternal(TableName tableName, TableConfig config, ConnectionDto connection) {
        byte[] table = istochnikService.generatedData(config);
        visorService.sendFile(tableName, table, config, connection);
    }
}
