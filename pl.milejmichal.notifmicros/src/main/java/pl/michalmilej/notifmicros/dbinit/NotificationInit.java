package pl.michalmilej.notifmicros.dbinit;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.michalmilej.notifmicros.notification.Notification;
import pl.michalmilej.notifmicros.notification.NotificationRepository;
import pl.michalmilej.notifmicros.notification.NotificationService;
import pl.michalmilej.notifmicros.notification.request.UpdateObservedUserIdsRequest;

@Profile("dbinit")
@Component
@RequiredArgsConstructor
public class NotificationInit {
    final NotificationRepository notificationRepository;
    final NotificationService notificationService;

    @PostConstruct
    public void clear() {
        notificationRepository.deleteAll();
    }

}
