package fr.cnamts.cpam33.traces.publisher.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TraceRabbitProperties.class)
public class RabbitConfiguration {

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean
    public Exchange traceExchange(TraceRabbitProperties traceRabbitProperties) {
        return ExchangeBuilder.topicExchange(traceRabbitProperties.exchange())
                .durable(true)
                .build();
    }

    public static final String EXCHANGE = "trace.topic";
    public static final String QUEUE = "trace.queue";
    public static final String DLQ = "trace.queue.dlq";
    public static final String ROUTING_PATTERN = "trace.#";


    @Bean("traceDLQ")
    Queue traceDlq() {
        return QueueBuilder.durable(DLQ).build();
    }

    @Bean("traceQueue")
    Queue traceQueue() {
        return QueueBuilder.durable(QUEUE)
                .deadLetterExchange("")
                .deadLetterRoutingKey(DLQ)
                .build();
    }

    @Bean("traceBinding")
    Binding traceBinding(Queue traceQueue, TopicExchange traceExchange) {
        return BindingBuilder.bind(traceQueue).to(traceExchange).with(ROUTING_PATTERN);
    }

}
