package pl.michalmilej.notifmicros.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.michalmilej.notifmicros.notification.request.NewPostCommentNotificationRequest;
import pl.michalmilej.notifmicros.notification.request.NewPostNotificationRequest;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class NotificationService {

    final NotificationRepository notificationRepository;

    public ResponseEntity<Void> addNewPostNotification(NewPostNotificationRequest request) {
        var userId = request.getAuthorId();
        var postId = request.getPostId();
        var interestedUsers = notificationRepository.findAll().stream().filter(
                userNotification -> userNotification.getObservedUserIds().contains(userId)).toList();
        interestedUsers.forEach(userNotification -> userNotification.getNewPostIds().add(postId));
        notificationRepository.saveAll(interestedUsers);
        return ResponseEntity.status(201).build();
    }

    public ResponseEntity<Notification> updateObservedUserIds(String userId, HashSet<String> observedUserIds) {
        var notification = notificationRepository.findById(userId);
        if (notification.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        notification.get().setObservedUserIds(observedUserIds);
        var savedNotification = notificationRepository.save(notification.get());
        return ResponseEntity.ok(savedNotification);
    }

    public ResponseEntity<Void> addPostNewCommentNotification(String postId, NewPostCommentNotificationRequest request) {
        var userNotification = notificationRepository.findById(request.getPostAuthorId());
        if (userNotification.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var postNewCommentIds = userNotification.get().getPostNewCommentIds();
        if (!postNewCommentIds.containsKey(postId)) {
            postNewCommentIds.put(postId, new HashSet<>());
            postNewCommentIds.get(postId).add(request.getCommentId());
        } else {
            postNewCommentIds.get(postId).add(request.getCommentId());
        }
        notificationRepository.save(userNotification.get());
        return ResponseEntity.status(201).build();
    }
}
