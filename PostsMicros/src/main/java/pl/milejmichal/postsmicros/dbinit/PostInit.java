package pl.milejmichal.postsmicros.dbinit;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.milejmichal.postsmicros.post.Post;
import pl.milejmichal.postsmicros.post.PostRepository;
import pl.milejmichal.postsmicros.post.PostService;
import pl.milejmichal.postsmicros.post.comment.PostComment;

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
        for (int i = 0, j = 0; i < 120; i++) {
            Post post = new Post("" + i,"" + i, "Post number " + i);
            var savedPost = postRepository.save(post);

            if (i % 5 == 0) {
                PostComment postComment = new PostComment("" + j++, "" + (i+1), "Comment 1");
                savedPost.getPostComments().add(postComment);
                savedPost = postRepository.save(savedPost);
            }
            if (i % 10 == 0) {
                PostComment postComment = new PostComment("" + j++, "" + (i+2), "Comment 2");
                savedPost.getPostComments().add(postComment);
                postRepository.save(savedPost);
            }

            if (i % 3 == 0) {
                post = new Post("" + ++i, "" + i, "Post number " + i );
                postRepository.save(post);
            }
        }
    }
}
