package pl.milejmichal.postsmicros.post.request;

import lombok.Data;

@Data
public class PostRequest {
    private String userId;
    private String text;
}
