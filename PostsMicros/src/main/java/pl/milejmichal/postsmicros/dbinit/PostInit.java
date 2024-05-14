package pl.milejmichal.postsmicros.dbinit;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.milejmichal.postsmicros.post.Post;
import pl.milejmichal.postsmicros.post.PostRepository;
import pl.milejmichal.postsmicros.post.PostService;

@Component
@RequiredArgsConstructor
@Profile("dbinit")
public class PostInit {
    final PostService postService;
    final PostRepository postRepository;

    @PostConstruct
    public void clear() {
        postRepository.deleteAll();
    }

    @Bean
    public void addPosts() {
        for (int i = 0; i < 100; i++) {
            Post post = new Post("" + i, "Post number " + i);
            postRepository.save(post);

            if (i % 3 == 0) {
                post = new Post("" + i, "I like pizza");
                postRepository.save(post);
            }
        }
    }
}
