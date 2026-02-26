package fr.cnamts.cpam33.traces.consumer.services;

import fr.cnamts.cpam33.traces.contract.dto.TraceDto;
import fr.cnamts.cpam33.traces.consumer.entities.TraceEntity;
import fr.cnamts.cpam33.traces.consumer.mappers.TraceIngestionMapper;
import fr.cnamts.cpam33.traces.consumer.repositories.TraceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TraceIngestService {

    private static final Logger logger = LoggerFactory.getLogger(TraceIngestService.class.getName());

    private final TraceRepository traceRepository;
    private final TraceIngestionMapper traceIngestionMapper;

    public record Result(boolean isAlreadyDigested) {}

    public TraceIngestService(TraceRepository traceRepository, TraceIngestionMapper traceIngestionMapper) {
        this.traceRepository = traceRepository;
        this.traceIngestionMapper = traceIngestionMapper;
    }

    public Result ingest(TraceDto traceDto) {
        TraceEntity traceEntity = traceIngestionMapper.toEntity(traceDto);
        logger.trace("ingesting trace {}", traceEntity);
        traceRepository.save(traceEntity);
        return new Result(false);
    }

}
