package pl.milejmichal.postsmicros.post;

import lombok.Data;

@Data
public class AddPostRequest {
    private String userId;
    private String text;
}
