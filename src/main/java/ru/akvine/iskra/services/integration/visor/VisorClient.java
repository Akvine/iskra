package ru.akvine.iskra.services.integration.visor;

import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.akvine.compozit.commons.*;
import ru.akvine.compozit.commons.dto.SuccessfulResponse;
import ru.akvine.compozit.commons.iskra.InsertValuesRequest;
import ru.akvine.compozit.commons.scripts.ExecuteScriptsRequest;
import ru.akvine.iskra.constants.ServiceTypeConstants;
import ru.akvine.iskra.services.integration.visor.dto.*;

@FeignClient(value = ServiceTypeConstants.VISOR)
public interface VisorClient {
    @PostMapping(value = "/databases/data/insert")
    void insertValues(@RequestBody @Valid InsertValuesRequest request);

    @PostMapping(value = "/databases/tables")
    GetTableResponse loadTables(@RequestBody @Valid ConnectionRequest request);

    @PostMapping(value = "/databases/columns")
    GetColumnsResponse loadColumns(@RequestBody @Valid GetColumnsRequest request);

    @PostMapping(value = "/metadata/constraints")
    ListConstraintsResponse loadConstraints(@RequestBody @Valid ListConstraintsRequest request);

    @PostMapping(value = "/scripts/execute")
    SuccessfulResponse execute(@RequestBody @Valid ExecuteScriptsRequest request);

    @PostMapping(value = "/scripts/generate/clear")
    GenerateClearScriptResponse generateClearScript(@RequestBody @Valid GenerateClearScriptRequest request);

    @PostMapping(value = "/databases/tables/related")
    GetRelatedTablesResponse getRelatedTables(@RequestBody @Valid GetRelatedTablesRequest request);
}
