package fr.cnamts.cpam33.traces.publisher.consumer.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "trace.rabbit")
public record TraceRabbitProperties(
        String exchange,
        String routingKeyTemplate,
        String routingPattern,
        List<String> queues,
        String dlq
) {

}
