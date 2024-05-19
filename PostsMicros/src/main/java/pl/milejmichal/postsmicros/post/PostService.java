package pl.milejmichal.postsmicros.post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.milejmichal.postsmicros.post.comment.Comment;
import pl.milejmichal.postsmicros.post.comment.CommentRequest;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    
    final PostRepository postRepository;

    public Post addPost(PostRequest postRequest) {
        Post post = new Post();
        post.setUserId(postRequest.getUserId());
        post.setText(postRequest.getText());
        return postRepository.save(post);
    }

    public ResponseEntity<Post> getPost(String postId) {
        var post = postRepository.findById(postId);
        return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Post> addComment(String postId, CommentRequest commentRequest) {
        var post = postRepository.findById(postId);
        if (post.isEmpty())
            return ResponseEntity.notFound().build();
        Comment comment = new Comment();
        comment.setId(UUID.randomUUID().toString());
        comment.setUserId(commentRequest.getUserId());
        comment.setText(commentRequest.getText());
        post.get().getComments().add(comment);
        return ResponseEntity.ok(postRepository.save(post.get()));
    }

    public List<Post> getUserPosts(String userId) {
        return postRepository.findAllByUserId(userId);
    }

    public List<Post> getPostsByIds(List<String> ids) {
        return postRepository.findByIdIn(ids);
    }
}
