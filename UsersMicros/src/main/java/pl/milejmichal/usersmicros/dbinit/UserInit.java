package pl.milejmichal.usersmicros.dbinit;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.milejmichal.usersmicros.communication.NotifMicrosCommunication;
import pl.milejmichal.usersmicros.user.User;
import pl.milejmichal.usersmicros.user.UserRepository;
import pl.milejmichal.usersmicros.user.UserService;
import pl.milejmichal.usersmicros.user.request.AddNotificationRequest;
import pl.milejmichal.usersmicros.user.request.UpdateObservedUserIdsRequest;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.LinkedList;

@Component
@RequiredArgsConstructor
@Profile("dbinit")
public class UserInit {
    final UserService userService;
    final UserRepository userRepository;

    final NotifMicrosCommunication notifMicrosCommunication;

    final int usersNumber = 1000;

    @PostConstruct
    public void clear() {
        userRepository.deleteAll();
    }

    @Bean
    public void initUsers() {
        // Create users
        for (int i = 0; i < usersNumber; i++) {
            userService.addUser("" + i, "user" + i);
        }

        // Add observed users
        // 1-10 famous people, everybody watches them
        HashSet<String> famousPeople = new HashSet<>();
        for (int i = 0; i < 10; i++) {
            famousPeople.add("" + i);
        }
        for (int i = 10; i < usersNumber; i++) {
            userService.addObservedUserIds("" + i, famousPeople);
        }

        sendDataToNotifMicros();
    }

    public void sendDataToNotifMicros() {
        LinkedList<User> users = userRepository.findAll();

        for (User user : users) {
            AddNotificationRequest addNotificationRequest = new AddNotificationRequest(user.getId());
            notifMicrosCommunication.addNotification(addNotificationRequest);

            String[] observedUserIds = user.getObservedUserIds().toArray(new String[0]);
            UpdateObservedUserIdsRequest updateObservedUserIdsRequest = new UpdateObservedUserIdsRequest(observedUserIds);
            notifMicrosCommunication.updateObservedUserIds(user.getId(), updateObservedUserIdsRequest);
        }
    }
}
