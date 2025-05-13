package ru.akvine.iskra.services.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class SystemDictionary {
    private String description;
    private List<String> values;
}
