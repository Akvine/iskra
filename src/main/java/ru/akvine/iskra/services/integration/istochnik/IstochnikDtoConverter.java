package ru.akvine.iskra.services.integration.istochnik;

import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.ColumnInfo;
import ru.akvine.compozit.commons.TableConfig;
import ru.akvine.compozit.commons.istochnik.GenerateTableRequest;

@Service
public class IstochnikDtoConverter {
    public GenerateTableRequest convertToGenerateTableRequest(TableConfig config) {
        return new GenerateTableRequest()
                .setSize(config.getSize())
                .setFileType("csv")
                .setColumns(config.getColumnConfigs().stream()
                        .map(ColumnInfo::getColumn)
                        .toList());
    }
}
