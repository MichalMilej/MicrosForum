package pl.milejmichal.postsmicros.post.websocket;

import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

@Component
public class PostWebSocketHandler extends TextWebSocketHandler {
    private WebSocketSession session;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) {
        this.session = session;
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status){
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
