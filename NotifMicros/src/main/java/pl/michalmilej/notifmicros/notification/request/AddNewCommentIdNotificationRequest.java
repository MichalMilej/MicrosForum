package pl.michalmilej.notifmicros.notification.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddNewCommentIdNotificationRequest {
    private String postAuthorId;
    private String commentId;
}
