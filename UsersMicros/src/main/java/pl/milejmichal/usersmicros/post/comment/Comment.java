package pl.milejmichal.usersmicros.post.comment;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Comment {
    @Id
    private String id;

    private String userId;

    private String text;
}
