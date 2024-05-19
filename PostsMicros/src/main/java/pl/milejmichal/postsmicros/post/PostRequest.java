package pl.milejmichal.postsmicros.post;

import lombok.Data;

@Data
public class PostRequest {
    private String userId;
    private String text;
}
