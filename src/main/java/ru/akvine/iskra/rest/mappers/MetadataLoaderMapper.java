package ru.akvine.iskra.rest.mappers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.iskra.repositories.dto.RelationsMatrix;
import ru.akvine.iskra.rest.dto.metadata.GenerateRelationsMatrixResponse;
import ru.akvine.iskra.rest.dto.table.ListTablesResponse;
import ru.akvine.iskra.services.domain.table.TableModel;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MetadataLoaderMapper {
    private final TableMapper tableMapper;

    public ListTablesResponse mapToListTablesResponse(List<TableModel> models) {
        return tableMapper.mapToListTablesResponse(models);
    }

    public GenerateRelationsMatrixResponse mapToGenerateRelationsMatrixResponse(RelationsMatrix matrix) {
        return new GenerateRelationsMatrixResponse()
                .setInfo(matrix);
    }
}
