package pl.milejmichal.postsmicros.post;

import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    final PostRepository postRepository;

    @QueryMapping
    public List<Post> getUserPosts(@Argument String userId) {
        return postRepository.findAllByUserId(userId);
    }
}
