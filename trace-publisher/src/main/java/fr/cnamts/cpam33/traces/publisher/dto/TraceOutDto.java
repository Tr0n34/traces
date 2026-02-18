package fr.cnamts.cpam33.traces.publisher.dto;

import java.util.List;

public record TraceOutDto(
        TraceStatusDto status,
        List<TraceAttributeDto> traceAttributes,
        TraceFailureDto error
) {
}