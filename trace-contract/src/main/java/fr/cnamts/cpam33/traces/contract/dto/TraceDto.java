package fr.cnamts.cpam33.traces.contract.dto;

import java.time.Instant;

public record TraceDto(
        String traceId,
        String schemaVersion,
        String applicationId,
        String acteMetierCode,
        String fonction,
        String correlationId,
        String frontPage,
        String boundedContext,
        String utilisateurId,
        String utilisateurIp,
        Instant createdOn,
        TraceInDto in,
        TraceOutDto out
) {

    @Override
    public String toString() {
        return "TraceDto{" +
                "traceId=" + traceId +
                ", schemaVersion=" + schemaVersion +
                ", applicationId=" + applicationId +
                ", acteMetierCode=" + acteMetierCode +
                ", fonction=" + fonction +
                ", correlationId=" + correlationId +
                ", frontPage=" + frontPage +
                ", boundedContext=" + boundedContext +
                ", utilisateurId=" + utilisateurId +
                ", utilisateurIp=" + utilisateurIp +
                ", createdOn=" + createdOn +
                ", in=" + in +
                ", out=" + out +
                '}';
    }

}
