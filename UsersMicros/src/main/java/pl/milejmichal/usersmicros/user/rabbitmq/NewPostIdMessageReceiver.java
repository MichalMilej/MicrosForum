package pl.milejmichal.usersmicros.user.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pl.milejmichal.usersmicros.user.UserService;
import pl.milejmichal.usersmicros.user.request.AddPostIdRequest;

@Component
@RequiredArgsConstructor
public class NewPostIdMessageReceiver {

    final UserService userService;

    @RabbitListener(queues = RabbitMQConfig.NEW_POST_IDS_QUEUE)
    public void receiveMessage(String message) {
        String[] parts = message.split(":");
        if (parts.length == 2) {
            String userId = parts[0];
            String postId = parts[1];
            System.out.println("Received new post id notification with userId: " + userId + " and postId: " + postId);
            userService.addNewPostId(userId, new AddPostIdRequest(postId));
        } else {
            System.err.println("Invalid message format: " + message);
        }
    }
}
