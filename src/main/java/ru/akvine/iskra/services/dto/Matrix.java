package ru.akvine.iskra.services.dto;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Matrix {
    private List<Row> rows;
}
