package pl.michalmilej.notifmicros.notification.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewPostNotificationRequest {
    private String authorId;
    private String postId;
}
