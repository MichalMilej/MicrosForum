package pl.milejmichal.postsmicros.post.comment;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("comments")
public class Comment {

    @Id
    private String id;

    private String userId;
    private String username;

    private String text;
}
