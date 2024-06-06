package pl.milejmichal.postsmicros.post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.milejmichal.postsmicros.post.comment.AddCommentRequest;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    final PostService postService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    Post addPost(@RequestBody AddPostRequest addPostRequest) {
        return postService.addPost(addPostRequest);
    }

    @PostMapping("/{postId}/comments")
    ResponseEntity<Post> addComment(@PathVariable String postId, @RequestBody AddCommentRequest addCommentRequest) {
        return postService.addComment(postId, addCommentRequest);
    }
}
