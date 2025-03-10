package mushroom.schedule.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // Exchange
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("scheduling_task");
    }

    // Queues
    @Bean
    public Queue startAuctions() {
        return new Queue("startAuctions");
    }

    @Bean
    public Queue completeAuctions() {
        return new Queue("completeAuctions");
    }

    @Bean
    public Queue createNoticeStartTime() {
        return new Queue("createNoticeStartTime");
    }

    @Bean
    public Queue createNoticeEndTime() {
        return new Queue("createNoticeEndTime");
    }

    // Bindings
    @Bean
    public Binding bindingStartAuctions(Queue startAuctions, DirectExchange directExchange) {
        return BindingBuilder.bind(startAuctions).to(directExchange).with("key_StartAuctions");
    }

    @Bean
    public Binding bindingCompleteAuctions(Queue completeAuctions, DirectExchange directExchange) {
        return BindingBuilder.bind(completeAuctions).to(directExchange).with("key_completeAuctions");
    }

    @Bean
    public Binding bindingCreateNoticeStartTime(Queue createNoticeStartTime, DirectExchange directExchange) {
        return BindingBuilder.bind(createNoticeStartTime).to(directExchange).with("key_createNoticeStartTime");
    }

    @Bean
    public Binding bindingCreateNoticeEndTime(Queue createNoticeEndTime, DirectExchange directExchange) {
        return BindingBuilder.bind(createNoticeEndTime).to(directExchange).with("key_CreateNoticeEndTime");
    }
}