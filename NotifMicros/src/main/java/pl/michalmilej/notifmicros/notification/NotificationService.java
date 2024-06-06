package pl.michalmilej.notifmicros.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.michalmilej.notifmicros.notification.rabbitmq.RabbitMQConfig;
import pl.michalmilej.notifmicros.notification.rabbitmq.RabbitMQSenderService;
import pl.michalmilej.notifmicros.notification.request.AddPostCommentNotificationRequest;
import pl.michalmilej.notifmicros.notification.request.AddPostNotificationRequest;
import pl.michalmilej.notifmicros.notification.request.AddNotificationRequest;
import pl.michalmilej.notifmicros.notification.request.UpdateObservedUserIdsRequest;
import pl.michalmilej.notifmicros.user.UserClient;
import pl.michalmilej.notifmicros.user.UserDTO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    final NotificationRepository notificationRepository;
    final UserClient userClient;

    final RabbitMQSenderService rabbitMQSenderService;

    public Notification addNotification(AddNotificationRequest addNotificationRequest) {
        return notificationRepository.save(new Notification(addNotificationRequest.getUserId()));
    }

    public ResponseEntity<Void> addPostNotification(AddPostNotificationRequest request) {
        var userId = request.getAuthorId();
        var postId = request.getPostId();
        var interestedUsers = notificationRepository.findAll().stream().filter(
                userNotification -> userNotification.getObservedUserIds().contains(userId)).toList();
        interestedUsers.forEach(userNotification -> userNotification.getNewPostIds().add(postId));
        notificationRepository.saveAll(interestedUsers);

        rabbitMQSenderService.addNewPostIdsToQueue();

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

    public ResponseEntity<Void> addPostCommentNotification(String postId, AddPostCommentNotificationRequest request) {
        var userNotification = notificationRepository.findById(request.getPostAuthorId());
        if (userNotification.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var newPostCommentIds = userNotification.get().getNewPostCommentIds();
        if (!newPostCommentIds.containsKey(postId)) {
            newPostCommentIds.put(postId, new HashSet<>());
            newPostCommentIds.get(postId).add(request.getCommentId());
        } else {
            newPostCommentIds.get(postId).add(request.getCommentId());
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
                .newPostCommentIds(new HashMap<>(userResponse.getPostNewCommentIdsMap()))
                .build();
    }

    @RabbitListener(queues = RabbitMQConfig.NEW_POST_COMMENT_IDS_QUEUE)
    public void receivePostNewCommentIdMessage(String message) {
        String[] parts = message.split(":");
        if (parts.length == 3) {
            String postId = parts[0];
            String postAuthorId = parts[1];
            String commentId = parts[2];
            System.out.println("Received new post commment id notification with postId: " + postId + " and commentId " + commentId);
            addPostCommentNotification(postId, new AddPostCommentNotificationRequest(postAuthorId, commentId));
        } else {
            System.err.println("Invalid message format: " + message);
        }
    }
}
