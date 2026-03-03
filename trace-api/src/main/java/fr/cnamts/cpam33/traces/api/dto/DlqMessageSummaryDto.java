package fr.cnamts.cpam33.traces.api.dto;

import fr.cnamts.cpam33.traces.api.configurations.DlqStatus;

import java.time.Instant;

public record DlqMessageSummaryDto(
        Long id,
        String sourceQueue,
        String status,
        Instant parkedAt,
        String contentType,
        String originalExchange,
        String originalRoutingKey
) {

}
