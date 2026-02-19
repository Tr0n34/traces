package fr.cnamts.cpam33.traces.publisher.consumer.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TraceRabbitProperties.class)
public class RabbitConsumerConfiguration {

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    TopicExchange tracesExchange(TraceRabbitProperties traceRabbitProperties) {
        return new TopicExchange(traceRabbitProperties.exchange(), true, false);
    }

    @Bean("tracesQueue")
    Queue tracesQueue(TraceRabbitProperties traceRabbitProperties) {
        return QueueBuilder.durable(traceRabbitProperties.queues().getFirst())
                .deadLetterExchange("")
                .deadLetterRoutingKey(traceRabbitProperties.dlq())
                .build();
    }

    @Bean("tracesDlq")
    Queue tracesDlq(TraceRabbitProperties traceRabbitProperties) {
        return QueueBuilder.durable(traceRabbitProperties.dlq()).build();
    }

    @Bean
    Binding tracesBinding(TraceRabbitProperties traceRabbitProperties, TopicExchange tracesExchange, Queue tracesQueue) {
        return BindingBuilder.bind(tracesQueue).to(tracesExchange).with(traceRabbitProperties.routingPattern());
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory,
                                                                               Jackson2JsonMessageConverter converter) {
        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);
        factory.setDefaultRequeueRejected(false);
        factory.setAdviceChain(RetryInterceptorBuilder.stateless()
                .maxAttempts(5)
                .backOffOptions(500, 2.0, 10_000)
                .recoverer(new RejectAndDontRequeueRecoverer())
                .build());
        return factory;
    }

}
