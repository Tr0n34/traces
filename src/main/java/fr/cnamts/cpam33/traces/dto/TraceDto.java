package fr.cnamts.cpam33.traces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.Map;

public record TraceDto(
        @NotBlank String traceId,
        @NotBlank String boundedContext,
        @NotBlank String acteMetierCode,
        @NotBlank String utilisateurId,
        @NotNull OffsetDateTime timestamp,
        Map<String, Object> context,
        Integer version
) {
}
