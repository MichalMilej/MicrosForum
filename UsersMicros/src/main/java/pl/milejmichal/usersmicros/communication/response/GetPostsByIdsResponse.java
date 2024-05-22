package pl.milejmichal.usersmicros.communication.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import pl.milejmichal.usersmicros.post.PostDTO;

import java.util.List;

@Data
public class GetPostsByIdsResponse {
    private Data data;

    @lombok.Data
    public static class Data {
        @JsonProperty("getPostsByIds")
        private List<PostDTO> postDTOS;
    }
}
