package fr.cnamts.cpam33.traces.consumer.mappers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.traces.contract.dto.TraceDto;
import fr.cnamts.cpam33.traces.consumer.entities.TraceEntity;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class TraceIngestionMapper {

    private final ObjectMapper objectMapper;
    private final Clock clock;

    public TraceIngestionMapper(ObjectMapper objectMapper, Clock clock) {
        this.objectMapper = objectMapper;
        this.clock = clock;
    }

    public TraceEntity toEntity(TraceDto dto) {
        Instant receivedAt = Instant.now(clock);

        return new TraceEntity()
                .setTraceId(dto.traceId())
                .setApplicationId(dto.applicationId())
                .setUtilisateurId(dto.utilisateurId())
                .setUtilisateurIp(dto.utilisateurIp())
                .setCorrelationId(dto.correlationId())
                .setFrontPage(dto.frontPage())
                .setBoundedContext(dto.boundedContext())
                .setFonction(dto.fonction())
                .setActeMetier(dto.acteMetierCode())
                .setReceivedAt(receivedAt)
                .setCreatedOn(dto.createdOn())
                .setTraceIn(writeJson(dto.in(), "trace.in"))
                .setTraceOut(writeJson(dto.out(), "trace.out"));
    }

    private Instant toInstant(LocalDateTime timestamp, ZoneId zoneId) {
        return timestamp != null
                ? timestamp.atZone(zoneId).toInstant()
                : null;
    }

    private String writeJson(Object value, String fieldName) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Unable to serialize " + fieldName + " as JSON", e);
        }
    }

}

