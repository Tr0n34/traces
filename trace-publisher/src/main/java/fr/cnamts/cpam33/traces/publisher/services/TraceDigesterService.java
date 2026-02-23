package fr.cnamts.cpam33.traces.publisher.services;

import fr.cnamts.cpam33.traces.contract.dto.TraceDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TraceDigesterService {

    private final RabbitTracePublisher rabbitTracePublisher;

    public TraceDigesterService(RabbitTracePublisher rabbitTracePublisher) {
        this.rabbitTracePublisher = rabbitTracePublisher;
    }

    @Transactional
    public void ingest(TraceDto dto) {
        rabbitTracePublisher.publish(dto);
    }

}
