package pl.michalmilej.notifmicros.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.michalmilej.notifmicros.notification.rabbitmq.RabbitMQNotificationService;
import pl.michalmilej.notifmicros.notification.request.NewPostCommentNotificationRequest;
import pl.michalmilej.notifmicros.notification.request.NewPostNotificationRequest;
import pl.michalmilej.notifmicros.notification.request.NotificationRequest;
import pl.michalmilej.notifmicros.notification.request.UpdateObservedUserIdsRequest;
import pl.michalmilej.notifmicros.user.UserClient;
import pl.michalmilej.notifmicros.user.UserDTO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class NotificationService {

    final NotificationRepository notificationRepository;
    final UserClient userClient;

    final RabbitMQNotificationService rabbitMQNotificationService;

    public Notification addNotification(NotificationRequest notificationRequest) {
        return notificationRepository.save(new Notification(notificationRequest.getUserId()));
    }

    public ResponseEntity<Void> addNewPostNotification(NewPostNotificationRequest request) {
        var userId = request.getAuthorId();
        var postId = request.getPostId();
        var interestedUsers = notificationRepository.findAll().stream().filter(
                userNotification -> userNotification.getObservedUserIds().contains(userId)).toList();
        interestedUsers.forEach(userNotification -> userNotification.getNewPostIds().add(postId));
        notificationRepository.saveAll(interestedUsers);

        rabbitMQNotificationService.addNewPostIdsToQueue();

        return ResponseEntity.status(201).build();
    }

    public ResponseEntity<Notification> updateObservedUserIds(String userId, UpdateObservedUserIdsRequest request) {
        var notification = notificationRepository.findById(userId);
        if (notification.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        notification.get().setObservedUserIds(new HashSet<>(Arrays.asList(request.getObservedUserIds())));
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

    public UserDTO getUserDetails(String userId) {
        var userResponse = userClient.getUser(userId);
        return UserDTO.builder()
                .id(userResponse.getId())
                .username(userResponse.getUsername())
                .email(userResponse.getEmail())
                .observedUserIds(new HashSet<>(userResponse.getObservedUserIdsList()))
                .newPostIds(new HashSet<>(userResponse.getNewPostIdsList()))
                .postNewCommentIds(new HashMap<>(userResponse.getPostNewCommentIdsMap()))
                .build();
    }
}
