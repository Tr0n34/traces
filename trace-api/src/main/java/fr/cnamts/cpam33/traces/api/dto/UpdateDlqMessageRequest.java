package fr.cnamts.cpam33.traces.api.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateDlqMessageRequest(
        @NotBlank String payloadBase64,
        @NotBlank String headersJson
) {}
