package pl.michalmilej.notifmicros.notification;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.michalmilej.notifmicros.notification.request.AddNewCommentIdNotificationRequest;
import pl.michalmilej.notifmicros.notification.request.AddNewPostIdNotificationRequest;
import pl.michalmilej.notifmicros.notification.request.AddNotificationRequest;
import pl.michalmilej.notifmicros.notification.request.UpdateObservedUserIdsRequest;
import pl.michalmilej.notifmicros.grpc.UserDTO;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationRESTController {

    final NotificationService notificationService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Notification addNotification(@RequestBody AddNotificationRequest addNotificationRequest) {
        return notificationService.addNotification(addNotificationRequest);
    }

    @PostMapping("/posts")
    public ResponseEntity<Void> addNewPostIdNotification(@RequestBody AddNewPostIdNotificationRequest request) {
        return notificationService.addPostNotification(request);
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<Void> addNewCommentIdNotification(@PathVariable String postId,
                                                            @RequestBody AddNewCommentIdNotificationRequest request) {
        return notificationService.addNewCommentIdNotification(postId, request);
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
