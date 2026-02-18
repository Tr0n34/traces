package fr.cnamts.cpam33.traces.publisher.dto;

import java.util.List;

public record TraceInDto(
        String method,
        String signature,
        List<TraceAttributeDto> params
) {
}