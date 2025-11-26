package ru.akvine.iskra.services.domain.statistics.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import ru.akvine.iskra.enums.ProcessState;
import ru.akvine.iskra.enums.SqlScriptType;

import java.util.Collection;

@Data
@Accessors(chain = true)
public class SearchStatisticsContext {
    private String processUuid;
    private SqlScriptType sqlScriptType;
    private Collection<ProcessState> states;
}
