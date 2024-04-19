package pl.milejmichal.postsmicros.post.comment;

import lombok.Data;

@Data
public class CommentRequest {
    String userId;
    String text;
}
