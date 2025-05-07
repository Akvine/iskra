package ru.akvine.iskra.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import ru.akvine.compozit.commons.ConnectionDto;
import ru.akvine.iskra.services.dto.GenerateDataAction;

@Getter
public class GenerateDataEvent extends ApplicationEvent {
    private final GenerateDataAction action;
    private final ConnectionDto connection;

    public GenerateDataEvent(Object source,
                             GenerateDataAction action,
                             ConnectionDto connection) {
        super(source);
        this.action = action;
        this.connection = connection;
    }
}
