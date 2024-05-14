package pl.milejmichal.postsmicros.post;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import pl.milejmichal.postsmicros.post.comment.Comment;

import java.util.LinkedList;

@Document("posts")
@Data
@NoArgsConstructor
public class Post {

    @Id
    private String id;

    private String userId;
    
    private String text;

    private LinkedList<Comment> comments = new LinkedList<>();

    public Post(String userId, String text) {
        this.userId = userId;
        this.text = text;
    }
}
