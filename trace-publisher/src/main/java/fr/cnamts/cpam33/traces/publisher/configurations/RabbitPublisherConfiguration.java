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
public class RabbitPublisherConfiguration {

    public static final String DEFAULT_EXCHANGE_DLX = "";

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean("rabbitTemplatePublisher")
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean("traceExchange")
    public Exchange traceExchange(TraceRabbitProperties traceRabbitProperties) {
        return ExchangeBuilder.topicExchange(traceRabbitProperties.exchange())
                .durable(true)
                .build();
    }

    @Bean("traceDlq")
    Queue traceDlq(TraceRabbitProperties traceRabbitProperties) {
        return QueueBuilder.durable(traceRabbitProperties.dlq()).build();
    }

    @Bean("traceQueue")
    Queue traceQueue(TraceRabbitProperties traceRabbitProperties) {
        return QueueBuilder.durable(traceRabbitProperties.queues().getFirst())
                .deadLetterExchange(DEFAULT_EXCHANGE_DLX)
                .deadLetterRoutingKey(traceRabbitProperties.dlq())
                .build();
    }

    @Bean("traceBinding")
    Binding traceBinding(TraceRabbitProperties traceRabbitProperties, Queue traceQueue, TopicExchange traceExchange) {
        return BindingBuilder.bind(traceQueue).to(traceExchange).with(traceRabbitProperties.routingPattern());
    }

}
