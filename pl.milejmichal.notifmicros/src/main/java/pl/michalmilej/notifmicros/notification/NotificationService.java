package pl.michalmilej.notifmicros.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.michalmilej.notifmicros.notification.request.NewPostNotificationRequest;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class NotificationService {

    final NotificationRepository notificationRepository;

    public ResponseEntity<Void> addNewPostNotification(NewPostNotificationRequest newPostNotificationRequest) {
        var userId = newPostNotificationRequest.getUserId();
        var postId = newPostNotificationRequest.getPostId();
        var interestedUsers = notificationRepository.findAll().stream().filter(
                userNotification -> userNotification.getObservedUsersIds().contains(userId)).toList();
        interestedUsers.forEach(userNotification -> userNotification.getNewPostsIds().add(postId));
        notificationRepository.saveAll(interestedUsers);
        return ResponseEntity.status(201).build();
    }

    public ResponseEntity<Notification> updateObservedUsersIds(String userId, HashSet<String> observedUserIds) {
        var notification = notificationRepository.findById(userId);
        if (notification.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        notification.get().setObservedUsersIds(observedUserIds);
        var savedNotification = notificationRepository.save(notification.get());
        return ResponseEntity.ok(savedNotification);
    }


}
