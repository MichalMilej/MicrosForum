package pl.michalmilej.notifmicros.notification;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.michalmilej.notifmicros.notification.request.NewPostCommentNotificationRequest;
import pl.michalmilej.notifmicros.notification.request.NewPostNotificationRequest;
import pl.michalmilej.notifmicros.notification.request.NotificationRequest;
import pl.michalmilej.notifmicros.notification.request.UpdateObservedUserIdsRequest;
import pl.michalmilej.notifmicros.user.UserDTO;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationRESTController {

    final NotificationService notificationService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Notification addNotification(@RequestBody NotificationRequest notificationRequest) {
        return notificationService.addNotification(notificationRequest);
    }

    @PostMapping("/posts")
    public ResponseEntity<Void> addNewPostNotification(@RequestBody NewPostNotificationRequest request) {
        return notificationService.addNewPostNotification(request);
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Void> addPostNewCommentNotification(@PathVariable String postId,
                                                              @RequestBody NewPostCommentNotificationRequest request) {
        return notificationService.addPostNewCommentNotification(postId, request);
    }

    @PatchMapping("/{userId}/observed-user-ids")
    public ResponseEntity<Notification> updateObservedUserIds(@PathVariable String userId,
                                                              @RequestBody UpdateObservedUserIdsRequest request) {
        return notificationService.updateObservedUserIds(userId, request);
    }

    @GetMapping("/{userId}/user-details")
    public UserDTO getUserDetails(@PathVariable String userId) {
        return notificationService.getUserDetails(userId);
    }
}
