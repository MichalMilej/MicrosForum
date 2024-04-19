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
import java.util.List;

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
        for (int i = 0; i < 1000; i++) {
            var user = new User("user"+i);
            user.setId(""+i);
            userRepository.save(user);
        }
    }

    @Bean
    public void initObservedUsersIds() {
        // Users 1-10 famous people
        for (int i = 0; i < 10; i++) {
            // Other users
            for (int j = 10; j < 1000; j++) {
                userService.addObservedUsersIds("" + j,
                        new HashSet<>(List.of("" + i)));
            }
        }
        for (int i = 0; i < 1000; i++) {
            userService.addObservedUsersIds("" + i,
                    new HashSet<>(List.of("" + (i+1), "" + (i+2))));
        }
    }
}
