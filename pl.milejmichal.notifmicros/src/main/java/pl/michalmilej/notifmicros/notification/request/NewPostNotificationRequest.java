package pl.michalmilej.notifmicros.notification.request;

import lombok.Data;

@Data
public class NewPostNotificationRequest {
    private String userId; // User who posted
    private String postId;
}
