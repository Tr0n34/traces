package fr.cnamts.cpam33.traces.publisher.mappers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.traces.contract.dto.TraceDto;
import fr.cnamts.cpam33.traces.publisher.configurations.TraceStatus;
import fr.cnamts.cpam33.traces.publisher.entities.TraceEntity;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.OffsetDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public class TraceDtoMapper {

    private final ObjectMapper objectMapper;

    public TraceDtoMapper(@Qualifier("traceObjectMapper") ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public TraceEntity toEntity(
            TraceDto dto,
            String reason,
            TraceStatus status,
            int retryCount,
            OffsetDateTime nextRetryAt,
            OffsetDateTime now) {
        JsonNode payload = objectMapper.valueToTree(dto);
        return new TraceEntity(
                null,
                dto.traceId(),
                dto.acteMetierCode(),
                payload,
                reason,
                status,
                retryCount,
                nextRetryAt,
                now,
                status == TraceStatus.PENDING ? now : null,
                null
        );
    }

    public TraceDto toDto(TraceEntity entity) {
        try {
            return objectMapper.treeToValue(
                    entity.getPayloadJson(),
                    TraceDto.class
            );
        } catch (Exception e) {
            throw new IllegalStateException(
                    "Impossible de reconstruire TraceDto depuis payload_json pour traceId="
                            + entity.getTraceId(),
                    e
            );
        }
    }

}
