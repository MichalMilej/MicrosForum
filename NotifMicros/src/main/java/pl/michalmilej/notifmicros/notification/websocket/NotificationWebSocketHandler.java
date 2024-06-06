package pl.michalmilej.notifmicros.notification.websocket;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import pl.michalmilej.notifmicros.notification.NotificationService;
import pl.michalmilej.notifmicros.notification.request.AddNewPostIdNotificationRequest;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class NotificationWebSocketHandler extends TextWebSocketHandler {

    private WebSocketSession session;

    final NotificationService notificationService;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        this.session = session;
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) {
        String payload = message.getPayload();
        String[] parts = payload.split(":");
        if (parts.length == 2) {
            String authorId = parts[0];
            String postId = parts[1];
            notificationService.addPostNotification(new AddNewPostIdNotificationRequest(authorId, postId));
        } else {
            System.err.println("Invalid message format: " + payload);
        }
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        if (this.session != null && this.session.equals(session)) {
            this.session = null;
        }
    }

    public void sendMessage(String message) {
        if (this.session != null && this.session.isOpen()) {
            try {
                this.session.sendMessage(new TextMessage(message));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}