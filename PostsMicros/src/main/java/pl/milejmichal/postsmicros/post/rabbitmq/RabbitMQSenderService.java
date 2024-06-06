package pl.milejmichal.postsmicros.post.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQSenderService {

    final TopicExchange newPostCommentIdsExchange;

    final RabbitTemplate rabbitTemplate;

    public void publishNewPostCommentId(String postId, String postAuthorId, String postCommentId) {
        String message = postId + ":" + postAuthorId + ":" + postCommentId;
        rabbitTemplate.convertAndSend(newPostCommentIdsExchange.getName(), "post.comment.created", message);
    }
}
