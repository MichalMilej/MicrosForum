package pl.milejmichal.usersmicros.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddNewPostIdRequest {
    private String postId;
}
