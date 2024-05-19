package pl.milejmichal.usersmicros.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.milejmichal.usersmicros.post.Post;
import pl.milejmichal.usersmicros.user.request.NewPostIdRequest;
import pl.milejmichal.usersmicros.user.request.AddUserRequest;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<User> getUser(@PathVariable String userId) {
        return userService.getUser(userId);
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody AddUserRequest addUserRequest) {
        return userService.addUser(addUserRequest);
    }

    @PostMapping("/{userId}/newPostIds")
    public ResponseEntity<User> addNewPostId(@PathVariable String userId, @RequestBody NewPostIdRequest newPostIdRequest) {
        return userService.addNewPostId(userId, newPostIdRequest);
    }

    @GetMapping("/{userId}/newPostIds")
    public ResponseEntity<List<Post>> getNewPosts(@PathVariable String userId) {
        return userService.getNewPosts(userId);
    }
}
