package pl.michalmilej.notifmicros.dbinit;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.michalmilej.notifmicros.notification.Notification;
import pl.michalmilej.notifmicros.notification.NotificationRepository;
import pl.michalmilej.notifmicros.notification.NotificationService;

import java.util.HashSet;
import java.util.List;

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

    @Bean
    public void initNotifications() {
        for (int i = 0; i < 1000; i++) {
            var notification = new Notification(""+i);
            notificationRepository.save(notification);
        }

        // Add observed users
        // 1-10 famous people, everybody watches them
        HashSet<String> famousPeopleIds = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            famousPeopleIds.add("" + i);
        }
        for (int j = 10; j < 1000; j++) {
            notificationService.updateObservedUsersIds("" + j, famousPeopleIds);
        }
    }

}
