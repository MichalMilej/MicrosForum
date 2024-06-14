package pl.milejmichal.usersmicros.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.milejmichal.usersmicros.communication.NotifMicrosCommunication;
import pl.milejmichal.usersmicros.communication.PostMicrosCommunication;
import pl.milejmichal.usersmicros.post.PostDTO;
import pl.milejmichal.usersmicros.user.request.AddUserRequest;
import pl.milejmichal.usersmicros.user.request.AddNewPostIdRequest;
import pl.milejmichal.usersmicros.user.request.AddNotificationRequest;
import pl.milejmichal.usersmicros.user.request.UpdateObservedUserIdsRequest;

import java.util.ArrayList;
import java.util.Arrays;
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
        var savedUser = userRepository.save(new User(addUserRequest.getUsername(), addUserRequest.getEmail()));

        notifMicrosCommunication.addNotification(new AddNotificationRequest(savedUser.getId()));

        return ResponseEntity.status(201).body(savedUser);
    }

    public ResponseEntity<User> getUser(String userId) {
        var user = userRepository.findById(userId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    public User addUser(String userId, String username) throws IllegalArgumentException {
        User user = new User(username);
        user.setId(userId);
        var savedUser = userRepository.save(user);

        notifMicrosCommunication.addNotification(new AddNotificationRequest(savedUser.getId()));
        return savedUser;
    }

    public User addUser(String userId, String username, String email) throws IllegalArgumentException {
        User user = new User(username);
        user.setId(userId);
        user.setEmail(email);
        var savedUser = userRepository.save(user);

        notifMicrosCommunication.addNotification(new AddNotificationRequest(savedUser.getId()));
        return savedUser;
    }

    public User addObservedUserIds(String userId, HashSet<String> userIdsToObserve) throws IllegalArgumentException {
        var user = userRepository.findById(userId);
        if (user.isEmpty())
            throw new IllegalArgumentException("User id not found!");
        for (var userIdToObserve : userIdsToObserve) {
            user.get().getObservedUserIds().add(userIdToObserve);
        }
        var savedUser = userRepository.save(user.get());

        String[] observedUserIds = savedUser.getObservedUserIds().toArray(new String[0]);
        notifMicrosCommunication.updateObservedUserIds(savedUser.getId(), new UpdateObservedUserIdsRequest(observedUserIds));

        return savedUser;
    }

    public User addObservedUserIdsREST(String userId, UpdateObservedUserIdsRequest updateObservedUserIdsRequest)
            throws IllegalArgumentException {
       HashSet<String> newObservedUserIds = new HashSet<>(Arrays.asList(updateObservedUserIdsRequest.getObservedUserIds()));
       return addObservedUserIds(userId, newObservedUserIds);
    }

    public ResponseEntity<User> addNewPostId(String userId, AddNewPostIdRequest addNewPostIdRequest) {
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        user.get().getNewPostIds().add(addNewPostIdRequest.getPostId());
        var savedUser = userRepository.save(user.get());
        return ResponseEntity.ok(savedUser);
    }

    public ResponseEntity<List<PostDTO>> getNewPosts(String userId) {
        var user = userRepository.findById(userId);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<PostDTO> postDTOS = postMicrosCommunication.getPosts(user.get().getNewPostIds());
        return ResponseEntity.ok(postDTOS);
    }
}
