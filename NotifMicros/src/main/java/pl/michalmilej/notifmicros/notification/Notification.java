package pl.michalmilej.notifmicros.notification;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.HashSet;

@Document("notifications")
@Data
public class Notification {

    @Id
    private String userId;

    private HashSet<String> observedUserIds = new HashSet<>();
    private HashSet<String> newPostIds = new HashSet<>();
    private HashMap<String, HashSet<String>> postNewCommentIds = new HashMap<>();

    public Notification(String userId) {
        this.userId = userId;
    }
}
