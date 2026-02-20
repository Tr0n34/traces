package fr.cnamts.cpam33.traces.consumer.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(TraceRabbitProperties.class)
public class RabbitConsumerConfiguration {

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    @RefreshScope
    TopicExchange tracesExchange(TraceRabbitProperties traceRabbitProperties) {
        return new TopicExchange(traceRabbitProperties.exchange(), true, false);
    }

    @Bean("tracesDlq")
    @RefreshScope
    Queue tracesDlq(TraceRabbitProperties traceRabbitProperties) {
        return QueueBuilder.durable(traceRabbitProperties.dlq()).build();
    }

    @Bean
    @RefreshScope
    public List<Queue> traceQueues(TraceRabbitProperties traceRabbitProperties) {
        return traceRabbitProperties.queues().stream()
                .map(name -> QueueBuilder.durable(name)
                        .deadLetterExchange("")
                        .deadLetterRoutingKey(traceRabbitProperties.dlq())
                        .build())
                .toList();
    }

    @Bean
    @RefreshScope
    public List<Binding> traceBindings(TraceRabbitProperties traceRabbitProperties,
                                       TopicExchange tracesExchange,
                                       List<Queue> traceQueues) {
        return traceQueues.stream()
                .map(q -> BindingBuilder.bind(q)
                        .to(tracesExchange)
                        .with(traceRabbitProperties.routingPattern()))
                .toList();
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
