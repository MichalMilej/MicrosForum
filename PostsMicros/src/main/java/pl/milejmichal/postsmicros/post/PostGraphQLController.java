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

    final PostRepository postRepository;

    @QueryMapping
    public List<Post> getUserPosts(@Argument String userId) {
        return postRepository.findAllByUserId(userId);
    }

    @QueryMapping
    public Optional<Post> getPost(@Argument String id) {
        return postRepository.findById(id);
    }

    @QueryMapping
    public List<Post> getPostsByIds(@Argument List<String> ids) {
        return postRepository.findByIdIn(ids);
    }
}
