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

    private HashSet<String> observedUsersIds = new HashSet<>();
    private HashSet<String> newPostsIds = new HashSet<>();
    private HashMap<String, String> postsNewCommentsIds = new HashMap<>();

    public User(String username) {
        this.username = username;
    }
}
