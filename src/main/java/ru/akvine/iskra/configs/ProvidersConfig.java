package ru.akvine.iskra.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.akvine.iskra.enums.NotificationServiceType;
import ru.akvine.iskra.enums.PlanState;
import ru.akvine.iskra.providers.NotificationServicesProvider;
import ru.akvine.iskra.providers.StateHandlersProvider;
import ru.akvine.iskra.services.NotificationService;
import ru.akvine.iskra.services.state_machine.handlers.PlanStateHandler;
import ru.akvine.iskra.services.impl.notifications.dto.NotificationPayload;

import java.util.List;
import java.util.Map;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Configuration
public class ProvidersConfig {

    @Bean
    public NotificationServicesProvider notificationServicesProvider(
            List<NotificationService<? extends NotificationPayload>> services) {
        Map<NotificationServiceType, NotificationService<? extends NotificationPayload>> servicesMap = services
                .stream()
                .collect(toMap(NotificationService::getType, identity()));
        return new NotificationServicesProvider(servicesMap);
    }

    @Bean
    public StateHandlersProvider stateHandlersProvider(List<PlanStateHandler> planStateHandlers) {
        Map<PlanState, PlanStateHandler> handlers = planStateHandlers
                .stream()
                .collect(toMap(PlanStateHandler::getCurrentState, identity()));
        return new StateHandlersProvider(handlers);
    }
}
