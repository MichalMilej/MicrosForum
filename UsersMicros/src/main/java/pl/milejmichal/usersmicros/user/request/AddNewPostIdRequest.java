package pl.milejmichal.usersmicros.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddNewPostIdRequest {
    private String postId;
}
