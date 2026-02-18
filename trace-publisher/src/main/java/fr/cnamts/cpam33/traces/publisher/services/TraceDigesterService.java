package fr.cnamts.cpam33.traces.publisher.services;

import fr.cnamts.cpam33.traces.publisher.dto.TraceDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class TraceDigesterService {

    public record Result(boolean isAlreadyDigested) {}

    private final RabbitTracePublisher rabbitTracePublisher;

    public TraceDigesterService(RabbitTracePublisher rabbitTracePublisher) {
        this.rabbitTracePublisher = rabbitTracePublisher;
    }

    @Transactional
    public Result ingest(TraceDto dto) {
        rabbitTracePublisher.publish(dto);
        return new Result(false);
    }

}
