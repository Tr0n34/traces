package fr.cnamts.cpam33.traces.publisher.consumer.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConsumerConfiguration {

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter
    ) {
        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter);

        // Important : ack “après succès”
        factory.setAcknowledgeMode(AcknowledgeMode.AUTO);

        // Concurrence
        factory.setConcurrentConsumers(2);
        factory.setMaxConcurrentConsumers(8);

        // Requeue = non (sinon boucle infinie)
        factory.setDefaultRequeueRejected(false);

        // Retry en mémoire (simple)
        factory.setAdviceChain(RetryInterceptorBuilder.stateless()
                .maxAttempts(5)
                .backOffOptions(500, 2.0, 10_000)
                .recoverer(new RejectAndDontRequeueRecoverer())
                .build());

        return factory;
    }

}
