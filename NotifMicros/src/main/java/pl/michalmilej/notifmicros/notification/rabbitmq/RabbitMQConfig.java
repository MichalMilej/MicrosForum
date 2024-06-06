package pl.michalmilej.notifmicros.notification.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String NEW_POST_IDS_QUEUE = "newPostIdsQueue";
    public static final String NEW_POST_COMMENT_IDS_EXCHANGE = "newPostCommentIdsExchange";
    public static final String NEW_POST_COMMENT_IDS_QUEUE = "newPostCommentIdsQueue";

    @Bean
    public Queue newPostIdsQueue() {
        return new Queue(NEW_POST_IDS_QUEUE, false);
    }

    @Bean
    public Queue newPostCommentIdsQueue() {
        return new Queue(NEW_POST_COMMENT_IDS_QUEUE);
    }

    @Bean
    public TopicExchange newPostCommentIdsExchange() {
        return new TopicExchange(NEW_POST_COMMENT_IDS_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue newPostCommentIdsQueue, TopicExchange newPostCommentIdsExchange) {
        return BindingBuilder.bind(newPostCommentIdsQueue).to(newPostCommentIdsExchange).with("post.comment.created");
    }
}
