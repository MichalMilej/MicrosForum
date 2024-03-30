package pl.milejmichal.usersmicros.user;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;

@Document("users")
@Data
public class User {

    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    private HashSet<Long> observedUsers = new HashSet<>();
    private HashSet<Long> newPosts = new HashSet<>();
    private HashSet<Long> newComments = new HashSet<>();

    public User(String username) {
        this.username = username;
    }
}
