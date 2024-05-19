package pl.milejmichal.postsmicros.post.comment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentRequest {
    String userId;
    String text;
}
