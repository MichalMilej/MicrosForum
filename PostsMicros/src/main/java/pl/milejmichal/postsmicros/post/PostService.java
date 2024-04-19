package pl.milejmichal.postsmicros.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {
    
    final PostRepository postRepository;

    public Post addPost(String userId, String text) {
        Post post = new Post();
        post.setUserId(userId);
        post.setText(text);
        return postRepository.save(post);
    }
}
