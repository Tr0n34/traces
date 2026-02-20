package fr.cnamts.cpam33.traces.consumer.services;

import fr.cnamts.cpam33.traces.contract.dto.TraceDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class TraceMessageListener {

    private static final Logger logger = LoggerFactory.getLogger(TraceMessageListener.class);

    private final TraceIngestService traceIngestService;

    public TraceMessageListener(TraceIngestService traceIngestService) {
        this.traceIngestService = traceIngestService;
    }

    @RabbitListener(
            id = "trace-consumer",
            queues = "#{'${trace.rabbit.queues}'.split(',')}",
            containerFactory = "rabbitListenerContainerFactory"
    )
    public void onMessage(TraceDto message) {
        logger.trace("Received trace message: {}", message);
        traceIngestService.ingest(message);
    }

}