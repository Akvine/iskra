package ru.akvine.iskra.services.impl.notifications;

import org.springframework.stereotype.Service;
import ru.akvine.iskra.enums.NotificationServiceType;
import ru.akvine.iskra.services.NotificationService;
import ru.akvine.iskra.services.impl.notifications.dto.EmailNotificationPayload;

@Service
public class EmailNotificationServiceImpl implements NotificationService<EmailNotificationPayload> {
    @Override
    public boolean notify(EmailNotificationPayload payload) {
        throw new UnsupportedOperationException(getType() + "notify type is not supported by app!");
    }

    @Override
    public NotificationServiceType getType() {
        return NotificationServiceType.EMAIL;
    }
}
