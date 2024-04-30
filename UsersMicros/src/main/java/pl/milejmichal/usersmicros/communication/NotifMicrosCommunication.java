package pl.milejmichal.usersmicros.communication;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import pl.milejmichal.usersmicros.user.request.AddNotificationRequest;
import pl.milejmichal.usersmicros.user.request.UpdateObservedUserIdsRequest;
import reactor.core.publisher.Mono;

@Component
public class NotifMicrosCommunication {
    final WebClient webClientNotifMicros = WebClient.create("http://localhost:8082/microsforum/notifmicros/notifications");

    public String addNotification(AddNotificationRequest addNotificationRequest) {
        return webClientNotifMicros.post()
                .body(Mono.just(addNotificationRequest), AddNotificationRequest.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String updateObservedUserIds(String userId, UpdateObservedUserIdsRequest updateObservedUserIdsRequest) {
        return webClientNotifMicros.patch()
                .uri("/" + userId + "/observed-user-ids")
                .body(Mono.just(updateObservedUserIdsRequest), UpdateObservedUserIdsRequest.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
