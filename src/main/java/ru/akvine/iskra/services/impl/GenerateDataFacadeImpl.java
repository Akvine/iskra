package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.Configuration;
import ru.akvine.compozit.commons.ConnectionDto;
import ru.akvine.compozit.commons.RelationsMatrixDto;
import ru.akvine.compozit.commons.TableConfig;
import ru.akvine.iskra.services.GenerateDataFacade;
import ru.akvine.iskra.services.dto.GenerateDataAction;
import ru.akvine.iskra.services.integration.istochnik.IstochnikService;
import ru.akvine.iskra.services.integration.visor.VisorService;

@Service
@RequiredArgsConstructor
public class GenerateDataFacadeImpl implements GenerateDataFacade {
    private final VisorService visorService;
    private final IstochnikService istochnikService;

    @Override
    public void generateData(GenerateDataAction action) {
        RelationsMatrixDto relationsMatrix = action.getRelationsMatrix();
        Configuration configuration = action.getConfiguration();
        ConnectionDto connection = action.getConnection();

        if (configuration.getTablesConfigs().isEmpty()) {
            return;
        }

        generateDataInternal(configuration.getTablesConfigs().getFirst(), connection);
    }

    private void generateDataInternal(TableConfig config, ConnectionDto connection) {
        byte[] table = istochnikService.generatedData(config);
        visorService.sendFile(table, config, connection);
    }
}
