package fr.cnamts.cpam33.traces.services;

import fr.cnamts.cpam33.traces.dto.TraceDto;
import fr.cnamts.cpam33.traces.entities.TraceEntity;
import fr.cnamts.cpam33.traces.repositories.TraceRepository;
import jakarta.transaction.Transactional;
import org.aspectj.weaver.tools.Trace;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TraceDigesterService {

    public record Result(boolean isAlreadyDigested) {}

    private final TraceRepository traceRepository;
    private final RabbitTracePublisher rabbitTracePublisher;

    public TraceDigesterService(TraceRepository traceRepository, RabbitTracePublisher rabbitTracePublisher) {
        this.traceRepository = traceRepository;
        this.rabbitTracePublisher = rabbitTracePublisher;
    }

    @Transactional
    public Result ingest(TraceDto dto) {
        try {
            traceRepository.save(new TraceEntity(
                    dto.traceId(),
                    Instant.now(),
                    dto.boundedContext(),
                    dto.acteMetierCode()
            ));
        } catch (DataIntegrityViolationException duplicate) {
            return new Result(true);
        }

        rabbitTracePublisher.publish(dto);
        return new Result(false);
    }

}
