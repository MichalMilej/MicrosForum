package pl.michalmilej.notifmicros.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.michalmilej.notifmicros.notification.rabbitmq.RabbitMQConfig;
import pl.michalmilej.notifmicros.notification.rabbitmq.RabbitMQSenderService;
import pl.michalmilej.notifmicros.notification.request.AddNewCommentIdNotificationRequest;
import pl.michalmilej.notifmicros.notification.request.AddNewPostIdNotificationRequest;
import pl.michalmilej.notifmicros.notification.request.AddNotificationRequest;
import pl.michalmilej.notifmicros.notification.request.UpdateObservedUserIdsRequest;
import pl.michalmilej.notifmicros.grpc.UserClient;
import pl.michalmilej.notifmicros.grpc.UserDTO;
import user.User;

import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class NotificationService {

    final NotificationRepository notificationRepository;
    final UserClient userClient;

    final RabbitMQSenderService rabbitMQSenderService;

    public Notification addNotification(AddNotificationRequest addNotificationRequest) {
        return notificationRepository.save(new Notification(addNotificationRequest.getUserId()));
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

    public ResponseEntity<Void> addPostNotification(AddNewPostIdNotificationRequest request) {
        var userId = request.getAuthorId();
        var postId = request.getPostId();
        var interestedUsers = notificationRepository.findAll().stream().filter(
                userNotification -> userNotification.getObservedUserIds().contains(userId)).toList();
        interestedUsers.forEach(userNotification -> userNotification.getNewPostIds().add(postId));
        notificationRepository.saveAll(interestedUsers);

        rabbitMQSenderService.addNewPostIdsToQueue();

        return ResponseEntity.status(201).build();
    }

    public ResponseEntity<Void> addNewCommentIdNotification(String postId, AddNewCommentIdNotificationRequest request) {
        var userNotification = notificationRepository.findById(request.getPostAuthorId());
        if (userNotification.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var newCommentIds = userNotification.get().getNewCommentIds();
        if (!newCommentIds.containsKey(postId)) {
            newCommentIds.put(postId, new HashSet<>());
            newCommentIds.get(postId).add(request.getCommentId());
        } else {
            newCommentIds.get(postId).add(request.getCommentId());
        }
        notificationRepository.save(userNotification.get());

        // Send notification to UsersMicros
        CompletableFuture<User.NotifyNewCommentIdResponse> futureResponse =
                userClient.notifyNewPostCommentId(postId, request.getPostAuthorId(), request.getCommentId());
        futureResponse.thenAccept(response -> {
            if (response.getSuccess()) {
                newCommentIds.get(postId).remove(request.getCommentId());
                System.out.println("New Comment id: " + request.getCommentId() + " successfully delivered.");
            }
        });

        return ResponseEntity.status(201).build();
    }

    public UserDTO getUserDetails(String userId) {
        var userResponse = userClient.getUser(userId);
        return UserDTO.builder()
                .id(userResponse.getId())
                .username(userResponse.getUsername())
                .email(userResponse.getEmail())
                .build();
    }

    @RabbitListener(queues = RabbitMQConfig.NEW_COMMENT_IDS_QUEUE)
    public void receiveNewCommentIdMessage(String message) {
        String[] parts = message.split(":");
        if (parts.length == 3) {
            String postId = parts[0];
            String postAuthorId = parts[1];
            String commentId = parts[2];
            System.out.println("Received new comment id notification with postId: " + postId + " and commentId " + commentId);
            addNewCommentIdNotification(postId, new AddNewCommentIdNotificationRequest(postAuthorId, commentId));
        } else {
            System.err.println("Invalid message format: " + message);
        }
    }
}
