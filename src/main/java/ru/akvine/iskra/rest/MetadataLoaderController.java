package ru.akvine.iskra.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.akvine.compozit.commons.dto.Response;
import ru.akvine.iskra.components.SecurityManager;
import ru.akvine.iskra.repositories.dto.RelationsMatrix;
import ru.akvine.iskra.rest.mappers.MetadataLoaderMapper;
import ru.akvine.iskra.rest.meta.MetadataLoaderMeta;
import ru.akvine.iskra.services.MetadataLoaderService;
import ru.akvine.iskra.services.domain.table.TableModel;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MetadataLoaderController implements MetadataLoaderMeta {
    private final MetadataLoaderService metadataLoaderService;
    private final MetadataLoaderMapper metadataLoaderMapper;
    private final SecurityManager securityManager;

    @Override
    public Response loadOrList(@RequestParam("planUuid") String planUuid) {
        List<TableModel> tables = metadataLoaderService.loadOrList(
                planUuid,
                securityManager.getCurrentUser().getUuid()
        );
        return metadataLoaderMapper.mapToListTablesResponse(tables);
    }

    @Override
    public Response generate(String planUuid) {
        RelationsMatrix matrix = metadataLoaderService.generate(planUuid, securityManager.getCurrentUser().getUuid());
        return metadataLoaderMapper.mapToGenerateRelationsMatrixResponse(matrix);
    }
}
