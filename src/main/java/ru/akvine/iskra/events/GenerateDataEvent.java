package ru.akvine.iskra.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.akvine.iskra.services.dto.GenerateDataAction;

@Getter
public class GenerateDataEvent extends ApplicationEvent {
    private final GenerateDataAction action;

    public GenerateDataEvent(Object source, GenerateDataAction action) {
        super(source);
        this.action = action;
    }
}
