package pl.michalmilej.notifmicros.user;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.HashSet;

@Data
@Builder
public class UserDTO {
    private String id;

    private String username;

    private String email;

    private HashSet<String> observedUserIds;

    private HashSet<String> newPostIds;

    private HashMap<String, String> newPostCommentIds;
}
