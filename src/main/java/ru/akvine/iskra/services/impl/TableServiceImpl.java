package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.repositories.TableRepository;
import ru.akvine.iskra.repositories.entities.PlanEntity;
import ru.akvine.iskra.repositories.entities.TableEntity;
import ru.akvine.iskra.services.PlanService;
import ru.akvine.iskra.services.TableService;
import ru.akvine.iskra.services.domain.ConnectionModel;
import ru.akvine.iskra.services.dto.TableMetadataDto;
import ru.akvine.iskra.services.dto.table.TableModel;
import ru.akvine.iskra.services.integration.visor.VisorService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TableServiceImpl implements TableService {
    private final VisorService visorService;
    private final TableRepository tableRepository;
    private final PlanService planService;

    @Override
    public List<TableModel> createOrListIfExist(String planUuid) {
        Asserts.isNotNull(planUuid);

        PlanEntity plan = planService.verifyExists(planUuid);

        if (tableRepository.countByPlan(planUuid) > 0) {
            return tableRepository.findAll(planUuid).stream().map(TableModel::new).toList();
        }

        List<TableMetadataDto> tables = visorService.loadTables(new ConnectionModel(plan.getConnection()));
        List<TableEntity> tablesToCreate = tables.stream().map(tableMetadataDto -> new TableEntity()
                .setName(tableMetadataDto.getTableName())
                .setSchema(tableMetadataDto.getSchema())
                .setDatabase(tableMetadataDto.getDatabase())
                .setPlan(plan)).toList();
        return tableRepository.saveAll(tablesToCreate).stream()
                .map(TableModel::new)
                .toList();
    }
}
