package pl.milejmichal.usersmicros.user;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.HashSet;

@Document("users")
@Data
public class User {

    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;

    private HashSet<String> observedUserIds = new HashSet<>();
    private HashSet<String> newPostIds = new HashSet<>();
    private HashMap<String, HashSet<String>> newCommentIds = new HashMap<>(); // First map for post id, second for comment ids

    public User() {}

    public User(String username) {
        this.username = username;
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}