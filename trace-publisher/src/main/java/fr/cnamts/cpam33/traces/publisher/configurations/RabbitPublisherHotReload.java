package fr.cnamts.cpam33.traces.publisher.configurations;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RabbitPublisherHotReload {

    private final AmqpAdmin admin;
    private final TopicExchange tracesExchange;
    private final Queue tracesDlq;
    private final List<Queue> traceQueues;
    private final List<Binding> traceBindings;

    public RabbitPublisherHotReload(
            AmqpAdmin admin,
            @Qualifier("tracesExchange") TopicExchange tracesExchange,
            @Qualifier("tracesDlq") Queue tracesDlq,
            List<Queue> traceQueues,
            List<Binding> traceBindings
    ) {
        this.admin = admin;
        this.tracesExchange = tracesExchange;
        this.tracesDlq = tracesDlq;
        this.traceQueues = traceQueues;
        this.traceBindings = traceBindings;
    }

    @EventListener(RefreshScopeRefreshedEvent.class)
    public void onRefresh() {
        admin.declareExchange(tracesExchange);
        admin.declareQueue(tracesDlq);
        traceQueues.forEach(admin::declareQueue);
        traceBindings.forEach(admin::declareBinding);
    }

}
