package fr.cnamts.cpam33.traces.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "trace.rabbit")
public record TraceRabbitProperties(
        String exchange,
        String routingKeyTemplate
) {

}
