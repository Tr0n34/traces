package fr.cnamts.cpam33.traces.publisher.consumer.services;

import fr.cnamts.cpam33.traces.contract.dto.TraceDto;
import fr.cnamts.cpam33.traces.publisher.consumer.entities.TraceEntity;
import fr.cnamts.cpam33.traces.publisher.consumer.mappers.TraceIngestionMapper;
import fr.cnamts.cpam33.traces.publisher.consumer.repositories.TraceRepository;
import org.springframework.stereotype.Service;

@Service
public class TraceIngestService {

    private final TraceRepository traceRepository;
    private final TraceIngestionMapper traceIngestionMapper;

    public record Result(boolean isAlreadyDigested) {}

    public TraceIngestService(TraceRepository traceRepository, TraceIngestionMapper traceIngestionMapper) {
        this.traceRepository = traceRepository;
        this.traceIngestionMapper = traceIngestionMapper;
    }

    public Result ingest(TraceDto traceDto) {
        TraceEntity traceEntity = traceIngestionMapper.toEntity(traceDto);
        traceRepository.save(traceEntity);
        return new Result(false);
    }

}
