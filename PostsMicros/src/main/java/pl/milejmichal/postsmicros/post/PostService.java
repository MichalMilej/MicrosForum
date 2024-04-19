package pl.milejmichal.postsmicros.post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.milejmichal.postsmicros.post.comment.Comment;

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

    public ResponseEntity<Post> getPost(String postId) {
        var post = postRepository.findById(postId);
        return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Post> addComment(String postId, String userId, String text) {
        var post = postRepository.findById(postId);
        if (post.isEmpty())
            return ResponseEntity.notFound().build();
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setText(text);
        post.get().getComments().add(comment);
        return ResponseEntity.ok(postRepository.save(post.get()));
    }
}
