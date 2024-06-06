package pl.milejmichal.postsmicros.post.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQSenderService {

    final TopicExchange newCommentIdsExchange;

    final RabbitTemplate rabbitTemplate;

    public void publishNewCommentId(String postId, String postAuthorId, String commentId) {
        String message = postId + ":" + postAuthorId + ":" + commentId;
        rabbitTemplate.convertAndSend(newCommentIdsExchange.getName(), "post.comment.created", message);
    }
}
