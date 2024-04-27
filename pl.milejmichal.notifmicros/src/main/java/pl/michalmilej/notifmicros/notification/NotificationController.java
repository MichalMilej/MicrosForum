package pl.michalmilej.notifmicros.notification;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.michalmilej.notifmicros.notification.request.NewPostCommentNotificationRequest;
import pl.michalmilej.notifmicros.notification.request.NewPostNotificationRequest;

import java.util.HashSet;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    final NotificationService notificationService;

    @PostMapping("/posts")
    public ResponseEntity<Void> addNewPostNotification(@RequestBody NewPostNotificationRequest request) {
        return notificationService.addNewPostNotification(request);
    }

    @PostMapping("/posts/{postId}")
    public ResponseEntity<Void> addPostNewCommentNotification(@PathVariable String postId,
                                                              @RequestBody NewPostCommentNotificationRequest request) {
        return notificationService.addPostNewCommentNotification(postId, request);
    }

    @PatchMapping("/{userId}/observed-user-ids")
    public ResponseEntity<Notification> updateObservedUserIds(@PathVariable String userId, @RequestBody HashSet<String> observedUserIds) {
        return notificationService.updateObservedUserIds(userId, observedUserIds);
    }
}
