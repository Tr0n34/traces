package fr.cnamts.cpam33.traces.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class TraceConsumerApplication {

    private static final Logger logger = LoggerFactory.getLogger(TraceConsumerApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(TraceConsumerApplication.class, args);
    }

    @Bean
    public ApplicationRunner declareTopologyAtStartup(
            AmqpAdmin admin,
            TopicExchange tracesExchange,
            @Qualifier("tracesDlq") Queue tracesDlq,
            List<Queue> traceQueues,
            List<Binding> traceBindings
    ) {
        return args -> {
            admin.declareExchange(tracesExchange);
            admin.declareQueue(tracesDlq);
            traceQueues.forEach(admin::declareQueue);
            traceBindings.forEach(admin::declareBinding);
            var names = traceQueues.stream().map(Queue::getName).toList();
            logger.info("declareTopologyAtStartup: queues={}", names);
        };
    }

}
