package pl.michalmilej.notifmicros.notification;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.michalmilej.notifmicros.notification.request.NewPostNotificationRequest;

import java.util.HashSet;

@RestController("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    final NotificationService notificationService;

    @PostMapping("/posts")
    public ResponseEntity<Void> addNewPostNotification(@RequestBody NewPostNotificationRequest newPostNotificationRequest) {
        return notificationService.addNewPostNotification(newPostNotificationRequest);
    }

    @PatchMapping("/{userId}/observed-users-ids")
    public ResponseEntity<Notification> updateObservedUsersIds(@PathVariable String userId, @RequestBody HashSet<String> observedUsersIds) {
        return notificationService.updateObservedUsersIds(userId, observedUsersIds);
    }
}
