package pl.michalmilej.notifmicros.notification.request;

import lombok.Data;

@Data
public class NewPostCommentNotificationRequest {
    private String postAuthorId;
    private String commentId;
}
