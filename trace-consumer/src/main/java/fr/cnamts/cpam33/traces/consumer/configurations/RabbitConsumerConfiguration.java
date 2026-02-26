package fr.cnamts.cpam33.traces.consumer.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableConfigurationProperties(TraceRabbitProperties.class)
public class RabbitConsumerConfiguration {

    /**
     * Bean de conversion JSON vers RabbitMQ
     * @param mapper L'<class>ObjectMapper</class> permettant la conversion
     * @return Un Mapper spécifique JSON Spring
     */
    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(ObjectMapper mapper) {
        return new Jackson2JsonMessageConverter(mapper);
    }

    /**
     * Bean de gestion AMQP pour Spring-RabbitMQ
     * @param connectionFactory Factory de connexion à RabbitMA
     * @return Le Bean d'administration du protocole d'échange de messages AMQP
     */
    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    /**
     * Bean de gestion d'un TopicExchange RabbitMQ
     * @param traceRabbitProperties Les propriétés RabbitMQ pour la gestion des traces
     * @return Le Topic d'échnage avec RabbitMQ
     * @apiNote <class>RefreshScope</class> Ce Bean se reconstruit à chaud lors d'une modification de
     * ses propriétés
     */
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

    /**
     * Bean de gestion de la liste des queues disponibles sur RabbitMQ</br>
     * Warning : Les N queues sont des miroirs strictement identiques (pas de sharding)
     * @param traceRabbitProperties Les propriétés RabbitMQ pour la gestion des traces
     * @return La liste des <class>Queue</class> disponibles en configuration
     * @apiNote <class>RefreshScope</class> Ce Bean se reconstruit à chaud lors d'une modification de
     * ses propriétés
     */
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

    /**
     * Bean de construction des bindings entre <class>Topic</class> et <class>Queue</class>
     * @param traceRabbitProperties Les propriétés RabbitMQ pour la gestion des traces
     * @param tracesExchange Le <class>TopicExchange</class> à bind
     * @param traceQueues Les <class>Queue</class> à bind
     * @return La liste des <class>Binding</class> construits
     * @apiNote <class>RefreshScope</class> Ce Bean se reconstruit à chaud lors d'une modification de
     * ses propriétés
     */
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

}
