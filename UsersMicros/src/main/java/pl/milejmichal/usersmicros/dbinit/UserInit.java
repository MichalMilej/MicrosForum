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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("dbinit")
public class UserInit {
    final UserService userService;
    final UserRepository userRepository;

    final NotifMicrosCommunication notifMicrosCommunication;


    @PostConstruct
    public void clear() {
        userRepository.deleteAll();
    }

    @Bean
    public void initUsers() {
        // Create users
        for (int i = 0; i < 1000; i++) {
            User user = userService.addUser("" + i, "user" + i, "user" + i + "@mail.com");
            // Add ids of new posts
            /*if (i < 15)
                user.getNewPostIds().addAll(List.of("0", "1", "2"));
            else if (i < 100)
                user.getNewPostIds().addAll(List.of("3", "4", "5"));*/
            userRepository.save(user);
        }

        // Add observed users
        // 1-5 famous people, everybody watches them
        HashSet<String> famousPeople = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            famousPeople.add("" + i);
        }
        for (int i = 5; i < 1000; i++) {
            userService.addObservedUserIds("" + i, famousPeople);
        }

        for (int i = 5; i < 500; i++) {
            userService.addObservedUserIds("" + i, new HashSet<>(List.of("" + (i+1), "" + (i+2), "" + (i+2))));
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
