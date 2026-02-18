package fr.cnamts.cpam33.traces.contract.dto;

import java.time.LocalDateTime;

public record TraceDto(
        String schemaVersion,
        String acteMetierCode,
        String boundedContext,
        String utilisateurId,
        LocalDateTime timestamp,
        TraceInDto in,
        TraceOutDto out
) {
}
