package pl.michalmilej.notifmicros.notification.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.michalmilej.notifmicros.notification.Notification;
import pl.michalmilej.notifmicros.notification.NotificationRepository;

import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RabbitMQNotificationService {

    final NotificationRepository notificationRepository;
    final RabbitTemplate rabbitTemplate;

    public void addNewPostIdsToQueue() {
        List<Notification> notifications = notificationRepository.getNotificationsWithNewPostIds();
        for (Notification notification : notifications) {
            HashSet<String> newPostIds = notification.getNewPostIds();
            newPostIds.forEach(postId -> {
                String message = notification.getUserId() + ":" + postId;
                rabbitTemplate.convertAndSend(RabbitMQConfig.NEW_POST_IDS_QUEUE, message);
            });
            newPostIds.clear();
            notificationRepository.save(notification);
        }
    }
}
