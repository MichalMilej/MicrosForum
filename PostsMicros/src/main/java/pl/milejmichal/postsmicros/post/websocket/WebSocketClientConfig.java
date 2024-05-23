package pl.milejmichal.postsmicros.post.websocket;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;

@Configuration
public class WebSocketClientConfig {

    @Bean
    public WebSocketConnectionManager webSocketConnectionManager(PostWebSocketHandler postWebSocketHandler) {
        String uri = "ws://localhost:8082/microsforum/notifmicros/notifications/posts/updates";
        StandardWebSocketClient client = new StandardWebSocketClient();
        WebSocketConnectionManager manager = new WebSocketConnectionManager(client, postWebSocketHandler, uri);
        manager.start();
        return manager;
    }

    @Bean
    public CommandLineRunner initiateWebSocketConnection(WebSocketConnectionManager manager) {
        return args -> {
            manager.start();
        };
    }
}


