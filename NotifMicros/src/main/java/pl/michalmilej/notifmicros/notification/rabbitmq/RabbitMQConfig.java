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
    public static final String NEW_COMMENT_IDS_EXCHANGE = "newCommentIdsExchange";
    public static final String NEW_COMMENT_IDS_QUEUE = "newCommentIdsQueue";

    @Bean
    public Queue newPostIdsQueue() {
        return new Queue(NEW_POST_IDS_QUEUE, false);
    }

    @Bean
    public Queue newCommentIdsQueue() {
        return new Queue(NEW_COMMENT_IDS_QUEUE);
    }

    @Bean
    public TopicExchange newCommentIdsExchange() {
        return new TopicExchange(NEW_COMMENT_IDS_EXCHANGE);
    }

    @Bean
    public Binding binding(Queue newCommentIdsQueue, TopicExchange newCommentIdsExchange) {
        return BindingBuilder.bind(newCommentIdsQueue).to(newCommentIdsExchange).with("post.comment.created");
    }
}
