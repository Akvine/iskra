package ru.akvine.iskra.services.impl.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.TableName;
import ru.akvine.iskra.configs.async.executors.TaskExecutor;
import ru.akvine.iskra.enums.GenerationStrategy;
import ru.akvine.iskra.services.GeneratorService;
import ru.akvine.iskra.services.MatrixService;
import ru.akvine.iskra.services.domain.table.TableModel;
import ru.akvine.iskra.services.dto.GenerateData;
import ru.akvine.iskra.services.dto.GenerateDataAction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;

@Service
@Slf4j
public class IndependentGenerationStrategyHandler extends AbstractGenerationStrategyHandler {
    private final GeneratorService generatorService;
    private final TaskExecutor taskExecutor;

    @Autowired
    public IndependentGenerationStrategyHandler(MatrixService matrixService,
                                                GeneratorService generatorService,
                                                TaskExecutor taskExecutor) {
        super(matrixService);
        this.generatorService = generatorService;
        this.taskExecutor = taskExecutor;
    }

    @Override
    public boolean generate(GenerateData action) {
        super.generate(action);

        Map<TableName, TableModel> selectedTables = action.getSelectedTables();
        Set<TableName> tableNames = action.getSelectedTables().keySet();
        for (TableName tableName : tableNames) {
            try {
                CompletableFuture.runAsync(
                        () -> {
                            GenerateDataAction generateDataAction = new GenerateDataAction()
                                    .setProcessUuid(action.getProcessUuid())
                                    .setUserUuid(action.getUserUuid())
                                    .setTable(selectedTables.get(tableName))
                                    .setResume(action.isResume());
                            generatorService.generate(generateDataAction);
                        },
                        taskExecutor.executor()
                );
            } catch (RejectedExecutionException exception) {
                log.info("Executor is full. Task was rejected");
            }
        }

        return true;
    }

    @Override
    public GenerationStrategy getStrategy() {
        return GenerationStrategy.INDEPENDENT;
    }
}
