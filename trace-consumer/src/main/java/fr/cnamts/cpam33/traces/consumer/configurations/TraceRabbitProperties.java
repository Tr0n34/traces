package fr.cnamts.cpam33.traces.consumer.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.List;

@RefreshScope
@ConfigurationProperties(prefix = "trace.rabbit")
public class TraceRabbitProperties {

    private String exchange;
    private String routingKeyTemplate;
    private String routingPattern;
    private List<String> queues;
    private String dlq;

    public String exchange() {
        return exchange;
    }

    public TraceRabbitProperties setExchange(String exchange) {
        this.exchange = exchange;
        return this;
    }

    public String routingKeyTemplate() {
        return routingKeyTemplate;
    }

    public TraceRabbitProperties setRoutingKeyTemplate(String routingKeyTemplate) {
        this.routingKeyTemplate = routingKeyTemplate;
        return this;
    }

    public String routingPattern() {
        return routingPattern;
    }

    public TraceRabbitProperties setRoutingPattern(String routingPattern) {
        this.routingPattern = routingPattern;
        return this;
    }

    public List<String> queues() {
        return queues;
    }

    public TraceRabbitProperties setQueues(List<String> queues) {
        this.queues = queues;
        return this;
    }

    public String dlq() {
        return dlq;
    }

    public TraceRabbitProperties setDlq(String dlq) {
        this.dlq = dlq;
        return this;
    }

}
