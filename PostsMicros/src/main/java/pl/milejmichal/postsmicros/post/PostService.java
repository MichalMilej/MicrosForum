package pl.milejmichal.postsmicros.post;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.milejmichal.postsmicros.post.comment.PostComment;
import pl.milejmichal.postsmicros.post.comment.AddPostCommentRequest;
import pl.milejmichal.postsmicros.post.rabbitmq.RabbitMQSenderService;
import pl.milejmichal.postsmicros.post.websocket.PostWebSocketHandler;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    
    final PostRepository postRepository;

    final PostWebSocketHandler postWebSocketHandler;

    final RabbitTemplate rabbitTemplate;

    final RabbitMQSenderService rabbitMQSenderService;


    public Post addPost(AddPostRequest addPostRequest) {
        Post post = new Post();
        post.setUserId(addPostRequest.getUserId());
        post.setText(addPostRequest.getText());
        var savedPost = postRepository.save(post);

        // UserId:PostId
        postWebSocketHandler.sendMessage(savedPost.getUserId() + ":" + savedPost.getId());
        System.out.println("New post from user " + savedPost.getUserId() + " with id "
                + savedPost.getId() + " sent to NotifMicros");

        return savedPost;
    }

    public ResponseEntity<Post> getPost(String postId) {
        var post = postRepository.findById(postId);
        return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Post> addPostComment(String postId, AddPostCommentRequest addPostCommentRequest) {
        var post = postRepository.findById(postId);
        if (post.isEmpty())
            return ResponseEntity.notFound().build();
        PostComment postComment = new PostComment();
        postComment.setId(UUID.randomUUID().toString());
        postComment.setUserId(addPostCommentRequest.getUserId());
        postComment.setText(addPostCommentRequest.getText());
        post.get().getPostComments().add(postComment);

        rabbitMQSenderService.publishNewPostCommentId(postId, post.get().getUserId(), postComment.getId());

        return ResponseEntity.ok(postRepository.save(post.get()));
    }

    public List<Post> getUserPosts(String userId) {
        return postRepository.findAllByUserId(userId);
    }

    public List<Post> getPostsByIds(List<String> ids) {
        return postRepository.findByIdIn(ids);
    }
}
