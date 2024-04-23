package pl.milejmichal.postsmicros.post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.milejmichal.postsmicros.post.comment.CommentRequest;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    final PostService postService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    Post addPost(@RequestBody PostRequest postRequest) {
        return postService.addPost(postRequest.getUserId(), postRequest.getText());
    }

    @PostMapping("/{postId}/comments")
    ResponseEntity<Post> addComment(@PathVariable String postId, @RequestBody CommentRequest commentRequest) {
        return postService.addComment(postId, commentRequest.getUserId(), commentRequest.getText());
    }
}