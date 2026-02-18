package fr.cnamts.cpam33.traces.publisher.consumer.services;

import fr.cnamts.cpam33.traces.contract.dto.TraceDto;
import fr.cnamts.cpam33.traces.publisher.consumer.configurations.RabbitTopologyConfiguration;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TraceMessageListener {

    private final TraceIngestService traceIngestService;

    public TraceMessageListener(TraceIngestService traceIngestService) {
        this.traceIngestService = traceIngestService;
    }

    @RabbitListener(queues = RabbitTopologyConfiguration.QUEUE, containerFactory = "rabbitListenerContainerFactory")
    public void onMessage(TraceDto message) {
        traceIngestService.ingest(message);
    }

}