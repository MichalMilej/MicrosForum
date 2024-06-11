package pl.milejmichal.postsmicros.post;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.milejmichal.postsmicros.post.comment.Comment;
import pl.milejmichal.postsmicros.post.comment.AddCommentRequest;
import pl.milejmichal.postsmicros.post.rabbitmq.RabbitMQSenderService;
import pl.milejmichal.postsmicros.post.websocket.PostWebSocketHandler;

import java.util.List;
import java.util.Optional;
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

    public ResponseEntity<Post> addComment(String postId, AddCommentRequest addCommentRequest) {
        var post = postRepository.findById(postId);
        if (post.isEmpty())
            return ResponseEntity.notFound().build();
        Comment comment = new Comment();
        comment.setId(UUID.randomUUID().toString());
        comment.setUserId(addCommentRequest.getUserId());
        comment.setText(addCommentRequest.getText());
        post.get().getComments().add(comment);

        rabbitMQSenderService.publishNewCommentId(postId, post.get().getUserId(), comment.getId());

        return ResponseEntity.ok(postRepository.save(post.get()));
    }

    public ResponseEntity<Post> getPost(String postId) {
        var post = postRepository.findById(postId);
        return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public Optional<Post> getPostGraphQL(String postId) {
        return postRepository.findById(postId);
    }

    public List<Post> getUserPosts(String userId) {
        return postRepository.findAllByUserId(userId);
    }

    public List<Post> getPostsByIds(List<String> ids) {
        return postRepository.findByIdIn(ids);
    }
}
