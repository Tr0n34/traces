package fr.cnamts.cpam33.traces.publisher.dto;

public record TraceFailureDto(
        String type,
        String message,
        String causeType,
        String causeMessage
) {
}
