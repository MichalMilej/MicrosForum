package pl.milejmichal.postsmicros.post.websocket;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Component
public class NotificationWebSocketClient implements CommandLineRunner {

    @Override
    public void run(String... args) {
        StandardWebSocketClient client = new StandardWebSocketClient();
        WebSocketHandler handler = new PostWebSocketHandler();

        WebSocketConnectionManager manager =
                new WebSocketConnectionManager(client,
                        handler,
                        "ws://localhost:8082/microsforum/notifmicros/notifications/posts/updates");
        manager.start();
    }
}
