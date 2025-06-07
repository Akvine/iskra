package ru.akvine.iskra.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.compozit.commons.utils.UUIDGenerator;
import ru.akvine.iskra.components.SecurityManager;
import ru.akvine.iskra.enums.GenerationStrategy;
import ru.akvine.iskra.exceptions.plan.RelationsMatrixNotGeneratedException;
import ru.akvine.iskra.providers.GenerationStrategyHandlersProvider;
import ru.akvine.iskra.services.GeneratorFacade;
import ru.akvine.iskra.services.GeneratorService;
import ru.akvine.iskra.services.MatrixService;
import ru.akvine.iskra.services.domain.plan.PlanModel;
import ru.akvine.iskra.services.domain.plan.PlanService;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.dto.GenerateData;
import ru.akvine.iskra.services.dto.plan.UpdatePlan;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeneratorFacadeImpl implements GeneratorFacade {
    private final GeneratorService generatorService;
    private final PlanService planService;
    private final SecurityManager securityManager;
    private final GenerationStrategyHandlersProvider provider;
    private final MatrixService matrixService;

    @Override
    public String generate(String planUuid, Map<TableName, TableModel> selectedTables, boolean resume) {
        Asserts.isNotNull(selectedTables);

        String userUuid = securityManager.getCurrentUser().getUuid();
        PlanModel plan;
        String processUuid;
        if (resume) {
            plan = new PlanModel(planService.verifyExists(planUuid, userUuid));
            processUuid = plan.getLastProcessUuid();
        } else {
            processUuid = UUIDGenerator.uuid();
            UpdatePlan updateAction = new UpdatePlan()
                    .setPlanUuid(planUuid)
                    .setUserUuid(userUuid)
                    .setLastProcessUuid(processUuid);
            plan = planService.update(updateAction);
        }

        if (plan.getRelationsMatrix() == null) {
            String errorMessage = String.format(
                    "Relations matrix for plan with uuid = [%s] not formed!",
                    planUuid
            );
            throw new RelationsMatrixNotGeneratedException(errorMessage);
        }

        List<String> independentTables = matrixService.detectIndependent(plan);
        if (CollectionUtils.isNotEmpty(independentTables)) {
            GenerateData action = new GenerateData()
                    .setPlan(plan)
                    .setProcessUuid(processUuid)
                    .setResume(resume)
                    .setUserUuid(userUuid)
                    .setSelectedTables(extractIndependentTables(
                            new HashSet<>(independentTables), selectedTables)
                    );
            provider.getByStrategy(GenerationStrategy.INDEPENDENT).generate(action);
        }

        return processUuid;
    }

    private Map<TableName, TableModel> extractIndependentTables(Set<String> detectedIndependentTables,
                                                                Map<TableName, TableModel> selectedTables) {
        Set<TableName> tables = detectedIndependentTables.stream().map(TableName::new).collect(Collectors.toSet());
        Map<TableName, TableModel> extractedTables = new HashMap<>();
        for (TableName tableName : tables) {
            if (selectedTables.containsKey(tableName)) {
                extractedTables.put(tableName, selectedTables.get(tableName));
            }
        }

        return extractedTables;
    }
}
