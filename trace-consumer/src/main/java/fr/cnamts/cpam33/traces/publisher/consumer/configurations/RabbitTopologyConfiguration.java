package fr.cnamts.cpam33.traces.publisher.consumer.configurations;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;

public class RabbitTopologyConfiguration {

    public static final String EXCHANGE = "trace.topic";
    public static final String QUEUE = "trace.queue";
    public static final String DLQ = "trace.queue.dlq";


    @Bean
    TopicExchange tracesExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    Queue tracesQueue() {
        return QueueBuilder.durable(QUEUE)
                .deadLetterExchange("")     // default exchange
                .deadLetterRoutingKey(DLQ)
                .build();
    }

    @Bean
    Queue tracesDlq() {
        return QueueBuilder.durable(DLQ).build();
    }

    @Bean
    Binding tracesBinding(TopicExchange tracesExchange, Queue tracesQueue) {
        return BindingBuilder.bind(tracesQueue).to(tracesExchange).with("trace.v1.#");
    }

}
