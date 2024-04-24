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

    private HashSet<String> observedUsersIds = new HashSet<>();
    private HashSet<String> newPostsIds = new HashSet<>();
    private HashMap<String, String> postsNewCommentsIds = new HashMap<>();

    public Notification(String userId) {
        this.userId = userId;
    }
}
