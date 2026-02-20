package fr.cnamts.cpam33.traces.publisher.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(TraceRabbitProperties.class)
public class RabbitPublisherConfiguration {

    public static final String DEFAULT_EXCHANGE_DLX = "";

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }


    @Bean("rabbitTemplatePublisher")
    @RefreshScope
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }

    @Bean("tracesExchange")
    @RefreshScope
    TopicExchange tracesExchange(TraceRabbitProperties traceRabbitProperties) {
        return new TopicExchange(traceRabbitProperties.exchange(), true, false);
    }

    @Bean("tracesDlq")
    @RefreshScope
    Queue traceDlq(TraceRabbitProperties traceRabbitProperties) {
        return QueueBuilder.durable(traceRabbitProperties.dlq()).build();
    }

    @Bean
    @RefreshScope
    public List<Queue> traceQueues(TraceRabbitProperties traceRabbitProperties) {
        return traceRabbitProperties.queues().stream()
                .map(name -> QueueBuilder.durable(name)
                        .deadLetterExchange(DEFAULT_EXCHANGE_DLX)
                        .deadLetterRoutingKey(traceRabbitProperties.dlq())
                        .build())
                .toList();
    }

    @Bean
    @RefreshScope
    public List<Binding> traceBindings(TraceRabbitProperties props,
                                       TopicExchange tracesExchange,
                                       List<Queue> traceQueues) {
        return traceQueues.stream()
                .map(q -> BindingBuilder.bind(q)
                        .to(tracesExchange)
                        .with(props.routingPattern()))
                .toList();
    }

}
