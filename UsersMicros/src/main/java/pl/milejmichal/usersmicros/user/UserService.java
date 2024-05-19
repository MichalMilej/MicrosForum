package pl.milejmichal.usersmicros.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.milejmichal.usersmicros.communication.NotifMicrosCommunication;
import pl.milejmichal.usersmicros.communication.PostMicrosCommunication;
import pl.milejmichal.usersmicros.post.Post;
import pl.milejmichal.usersmicros.user.request.AddUserRequest;
import pl.milejmichal.usersmicros.user.request.NewPostIdRequest;
import pl.milejmichal.usersmicros.user.request.AddNotificationRequest;
import pl.milejmichal.usersmicros.user.request.UpdateObservedUserIdsRequest;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    final UserRepository userRepository;

    final NotifMicrosCommunication notifMicrosCommunication;
    final PostMicrosCommunication postMicrosCommunication;


    public ResponseEntity<User> addUser(AddUserRequest addUserRequest) {
        if (userRepository.findByUsername(addUserRequest.getUsername()).isPresent())
            return ResponseEntity.status(409).build();
        var savedUser = userRepository.save(new User(addUserRequest.getUsername()));

        notifMicrosCommunication.addNotification(new AddNotificationRequest(savedUser.getId()));

        return ResponseEntity.status(201).body(savedUser);
    }

    public User addUser(String userId, String username) throws IllegalArgumentException {
        User user = new User(username);
        user.setId(userId);
        var savedUser = userRepository.save(user);

        notifMicrosCommunication.addNotification(new AddNotificationRequest(savedUser.getId()));
        return savedUser;
    }

    public User addObservedUserIds(String userId, HashSet<String> userIdsToObserve) throws IllegalArgumentException {
        var user = userRepository.findById(userId);
        if (user.isEmpty())
            throw new IllegalArgumentException("User id not found!");
        for (var userIdToObserve : userIdsToObserve) {
            if (userRepository.findById(userIdToObserve).isPresent()) {
                user.get().getObservedUserIds().add(userIdToObserve);
            }
        }
        var savedUser = userRepository.save(user.get());

        String[] observedUserIds = savedUser.getObservedUserIds().toArray(new String[0]);
        notifMicrosCommunication.updateObservedUserIds(savedUser.getId(), new UpdateObservedUserIdsRequest(observedUserIds));

        return savedUser;
    }

    public ResponseEntity<User> getUser(String userId) {
        var user = userRepository.findById(userId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<User> addNewPostId(String userId, NewPostIdRequest newPostIdRequest) {
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        user.get().getNewPostIds().add(newPostIdRequest.getPostId());
        var savedUser = userRepository.save(user.get());
        return ResponseEntity.ok(savedUser);
    }

    public ResponseEntity<List<Post>> getNewPosts(String userId) {
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Post> posts = postMicrosCommunication.getPosts(user.get().getNewPostIds());
        return ResponseEntity.ok(posts);
    }
}
