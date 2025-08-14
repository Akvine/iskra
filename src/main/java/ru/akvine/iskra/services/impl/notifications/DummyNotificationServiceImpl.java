package ru.akvine.iskra.services.impl.notifications;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.akvine.compozit.commons.utils.Asserts;
import ru.akvine.iskra.enums.NotificationServiceType;
import ru.akvine.iskra.services.NotificationService;
import ru.akvine.iskra.services.impl.notifications.dto.NotificationPayload;
import ru.akvine.iskra.utils.StringHelper;

@Service
@Slf4j
public class DummyNotificationServiceImpl implements NotificationService<NotificationPayload> {
    @Override
    public boolean notify(NotificationPayload payload) {
        Asserts.isNotNull(payload);
        Asserts.isNotBlank(payload.getTarget());

        log.info("Successful notify {}", StringHelper.mask(payload.getTarget()));
        return true;
    }

    @Override
    public NotificationServiceType getType() {
        return NotificationServiceType.DUMMY;
    }
}
