package pl.milejmichal.usersmicros.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    final UserRepository userRepository;

    User createUser(UserRequest userRequest) {
        System.out.println("Creating user: " + userRequest.getUsername());
        return userRepository.save(new User(userRequest.getUsername()));
    }
}
