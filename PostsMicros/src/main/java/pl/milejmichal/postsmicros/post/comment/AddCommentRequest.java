package pl.milejmichal.postsmicros.post.comment;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddCommentRequest {
    String userId;
    String text;
}
