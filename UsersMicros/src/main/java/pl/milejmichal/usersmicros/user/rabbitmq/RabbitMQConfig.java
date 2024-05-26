package pl.milejmichal.usersmicros.user.rabbitmq;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String NEW_POST_IDS_QUEUE = "newPostIdsQueue";

    @Bean
    public Queue postQueue() {
        return new Queue(NEW_POST_IDS_QUEUE, false);
    }
}
