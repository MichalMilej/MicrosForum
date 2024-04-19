package pl.milejmichal.usersmicros.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class UserService {
    final UserRepository userRepository;

    public User addUser(String username) throws IllegalArgumentException {
        if (userRepository.findByUsername(username).isPresent())
            throw new IllegalArgumentException("Username already exists!");
        return userRepository.save(new User(username));
    }

    public User addObservedUsersIds(String userId, HashSet<String> usersIdsToObserve) throws IllegalArgumentException {
        var user = userRepository.findById(userId);
        if (user.isEmpty())
            throw new IllegalArgumentException("User id not found!");
        for (var userIdToObserve : usersIdsToObserve) {
            if (userRepository.findByUsername(userIdToObserve).isPresent()) {
                user.get().getObservedUsersIds().add(userIdToObserve);
            }
        }
        return userRepository.save(user.get());
    }

    public ResponseEntity<User> getUser(String userId) {
        var user = userRepository.findById(userId);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
