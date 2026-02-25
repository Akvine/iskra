package ru.akvine.iskra.rest.mappers;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.akvine.iskra.rest.dto.table.ListTablesResponse;
import ru.akvine.iskra.services.domain.table.TableModel;

@Component
@RequiredArgsConstructor
public class MetadataLoaderMapper {
    private final TableMapper tableMapper;

    public ListTablesResponse mapToListTablesResponse(List<TableModel> models) {
        return tableMapper.mapToListTablesResponse(models);
    }
}
