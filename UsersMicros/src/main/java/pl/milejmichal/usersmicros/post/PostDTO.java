package pl.milejmichal.usersmicros.post;

import lombok.Data;
import pl.milejmichal.usersmicros.post.comment.Comment;

import java.util.LinkedList;

@Data
public class PostDTO {

    private String id;

    private String userId;

    private String text;

    private LinkedList<Comment> comments = new LinkedList<>();
}
