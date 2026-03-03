package fr.cnamts.cpam33.traces.api.dto;

import java.time.Instant;

public record DlqMessageDetailDto(
        Long id,
        String sourceQueue,
        String status,
        Instant parkedAt,
        Instant updatedAt,
        String contentType,
        String payloadBase64,
        String headersJson,
        String originalExchange,
        String originalRoutingKey,
        String lastError,
        Integer republishAttempts,
        Instant lastRepublishAt
) {}
