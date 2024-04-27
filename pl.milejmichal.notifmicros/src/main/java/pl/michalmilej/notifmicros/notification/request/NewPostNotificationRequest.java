package pl.michalmilej.notifmicros.notification.request;

import lombok.Data;

@Data
public class NewPostNotificationRequest {
    private String authorId;
    private String postId;
}
