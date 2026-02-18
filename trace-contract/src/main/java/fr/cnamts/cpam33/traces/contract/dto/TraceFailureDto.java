package fr.cnamts.cpam33.traces.contract.dto;

public record TraceFailureDto(
        String type,
        String message,
        String causeType,
        String causeMessage
) {
}
