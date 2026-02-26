package fr.cnamts.cpam33.traces.consumer.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RabbitListenerHotReload {

    private final static Logger logger = LoggerFactory.getLogger(RabbitListenerHotReload.class);

    private final RabbitListenerEndpointRegistry registry;
    private final AmqpAdmin admin;
    private final TopicExchange tracesExchange;
    private final Queue tracesDlq;
    private final List<Queue> traceQueues;
    private final List<Binding> traceBindings;

    public RabbitListenerHotReload(RabbitListenerEndpointRegistry registry,
                                   AmqpAdmin admin,
                                   TopicExchange tracesExchange,
                                   @Qualifier("tracesDlq") Queue tracesDlq,
                                   List<Queue> traceQueues,
                                   List<Binding> traceBindings) {
        this.registry = registry;
        this.admin = admin;
        this.tracesExchange = tracesExchange;
        this.tracesDlq = tracesDlq;
        this.traceQueues = traceQueues;
        this.traceBindings = traceBindings;
    }

    @EventListener(RefreshScopeRefreshedEvent.class)
    public void onRefresh() {
        var container = registry.getListenerContainer("trace-consumer");
        container.stop();
        admin.declareExchange(tracesExchange);
        admin.declareQueue(tracesDlq);
        traceQueues.forEach(admin::declareQueue);
        traceBindings.forEach(admin::declareBinding);
        container.start();
        logger.info("RabbitListenerHotReload started");
    }

}

