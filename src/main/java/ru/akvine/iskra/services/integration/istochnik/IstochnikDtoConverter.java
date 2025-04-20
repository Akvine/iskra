package ru.akvine.iskra.services.integration.istochnik;

import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.ColumnConfig;
import ru.akvine.compozit.commons.TableConfig;
import ru.akvine.compozit.commons.istochnik.ColumnDto;
import ru.akvine.compozit.commons.istochnik.GenerateTableRequest;

@Service
public class IstochnikDtoConverter {
    public GenerateTableRequest convertToGenerateTableRequest(TableConfig config) {
        return new GenerateTableRequest()
                .setSize(config.getSize())
                .setFileType("csv")
                .setColumns(null);
    }

    private ColumnDto buildColumnDto(ColumnConfig config) {
        return new ColumnDto()
                .setName(config.getName())
                .setConfig(null);
    }
}
