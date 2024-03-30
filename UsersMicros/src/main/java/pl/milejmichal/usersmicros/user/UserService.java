package pl.milejmichal.usersmicros.user;

import lombok.RequiredArgsConstructor;
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

    public User addObservedUsernames(String username, HashSet<String> usernamesToObserve) throws IllegalArgumentException {
        var user = userRepository.findByUsername(username);
        if (user.isEmpty())
            throw new IllegalArgumentException("Username not found!");
        for (var usernameToObserve : usernamesToObserve) {
            if (userRepository.findByUsername(usernameToObserve).isPresent()) {
                user.get().getObservedUsernames().add(usernameToObserve);
            }
        }
        return userRepository.save(user.get());
    }
}
