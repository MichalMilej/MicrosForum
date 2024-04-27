package pl.milejmichal.usersmicros.dbinit;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.milejmichal.usersmicros.user.User;
import pl.milejmichal.usersmicros.user.UserRepository;
import pl.milejmichal.usersmicros.user.UserService;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
@Profile("dbinit")
public class UserInit {
    final UserService userService;
    final UserRepository userRepository;

    @PostConstruct
    public void clear() {
        userRepository.deleteAll();
    }

    @Bean
    public void initUsers() {
        // Create users
        for (int i = 0; i < 1000; i++) {
            var user = new User("user" + i);
            user.setId("" + i);
            userRepository.save(user);
        }

        // Add observed users
        // 1-10 famous people, everybody watches them
        HashSet<String> famousPeople = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            famousPeople.add("" + i);
        }
        for (int i = 10; i < 1000; i++) {
            userService.addObservedUserIds("" + i, famousPeople);
        }
    }
}
