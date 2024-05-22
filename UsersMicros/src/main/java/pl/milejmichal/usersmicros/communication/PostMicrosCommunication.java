package pl.milejmichal.usersmicros.communication;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.milejmichal.usersmicros.communication.response.GetPostsByIdsResponse;
import pl.milejmichal.usersmicros.post.PostDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Component
public class PostMicrosCommunication {

    final WebClient webClientPostsMicros = WebClient.create("http://localhost:8081/microsforum/postsmicros");


    public List<PostDTO> getPosts(HashSet<String> postIds) {
        String query = """
                query getPostsByIds($ids: [ID!]!) {
                    getPostsByIds(ids: $ids) {
                        id,
                        userId,
                        text,
                        comments {
                            id,
                            userId,
                            text
                        }
                    }
                }
                """;
        Map<String, Object> variables = Map.of("ids", postIds);
        Map<String, Object> body = Map.of(
                "query", query,
                "variables", variables
        );

        var getPostByIdsResponse = webClientPostsMicros.post()
                .uri("/graphql")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(GetPostsByIdsResponse.class)
                .block();

        return getPostByIdsResponse.getData().getPostDTOS();
    }
}
