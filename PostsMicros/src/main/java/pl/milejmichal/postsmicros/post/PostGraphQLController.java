package pl.milejmichal.postsmicros.post;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PostGraphQLController {

    final PostService postService;

    @QueryMapping
    public List<Post> getUserPosts(@Argument String userId) {
        return postService.getUserPosts(userId);
    }

    @QueryMapping
    public Optional<Post> getPost(@Argument String id) {
        return postService.getPostGraphQL(id);
    }

    @QueryMapping
    public List<Post> getPostsByIds(@Argument List<String> ids) {
        return postService.getPostsByIds(ids);
    }
}
