package ru.akvine.iskra.services.impl;

import org.springframework.stereotype.Service;
import ru.akvine.iskra.repositories.dto.RelationsMatrix;
import ru.akvine.iskra.services.MatrixService;
import ru.akvine.iskra.services.domain.plan.PlanModel;

import java.util.ArrayList;
import java.util.List;

@Service
public class MatrixServiceImpl implements MatrixService {
    @Override
    public List<String> detectIndependent(PlanModel plan) {
        RelationsMatrix matrix = plan.getRelationsMatrix();
        List<String> independentTables = new ArrayList<>();

        List<String> columns = matrix.getMatrix().keySet().stream().toList();
        for (String columnName : columns) {
            if (!matrix.hasRelationsByColumn(columnName) && !matrix.hasRelationsByRow(columnName)) {
                independentTables.add(columnName);
            }
        }

        return independentTables;
    }
}
