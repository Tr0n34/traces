package fr.cnamts.cpam33.traces.contract.policies;

import fr.cnamts.cpam33.traces.contract.dto.TraceDto;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

import static fr.cnamts.cpam33.traces.contract.policies.Policy.*;

public final class TracePolicy {

    private TracePolicy() {}

    public static Policy<TraceDto> forPublish(Clock clock) {
        return forPublish(clock, TracePolicyOptions.defaults());
    }

    public static Policy<TraceDto> forPublish(Clock clock, TracePolicyOptions options) {
        Objects.requireNonNull(clock, "clock");
        Objects.requireNonNull(options, "options");
        Policy<TraceDto> required = Policy.<TraceDto>alwaysOk()
                .and(requireNonBlank("TRACE_ID_REQUIRED", TraceDto::traceId, "traceId est requis"))
                .and(requireNonBlank("SCHEMA_REQUIRED", TraceDto::schemaVersion, "schemaVersion est requis"))
                .and(requireNonBlank("EVENT_CODE_REQUIRED", TraceDto::acteMetierCode, "acteMetierCode est requis"))
                .and(requireNonBlank("BOUNDED_CONTEXT_REQUIRED", TraceDto::boundedContext, "boundedContext est requis"))
                .and(requireNonNull("TIMESTAMP_REQUIRED", TraceDto::createdOn, "createdOn est requis"));
        Policy<TraceDto> sizes = Policy.<TraceDto>alwaysOk()
                .and(requireMaxLength("TRACE_ID_TOO_LONG", TraceDto::traceId, 80, "traceId max 80"));
        Policy<TraceDto> timestampNotTooFuture = Policy.<TraceDto>ofRule(
                "TIMESTAMP_IN_FUTURE",
                dto -> notTooFuture(dto.createdOn(), clock, options.futureTolerance()),
                "timestamp est dans le futur (tolérance " + options.futureTolerance().toMinutes() + " minutes)"
        );

        Policy<TraceDto> schemaAllowed = requireOneOf(
                "SCHEMA_NOT_ALLOWED",
                TraceDto::schemaVersion,
                options.allowedSchemas(),
                "schemaVersion non supporté"
        );

        return required
                .and(sizes)
                .and(timestampNotTooFuture)
                .and(schemaAllowed);
    }

    public static Policy<TraceDto> forConsume() {
        return forConsume(TracePolicyOptions.defaults());
    }

    public static Policy<TraceDto> forConsume(TracePolicyOptions options) {
        Objects.requireNonNull(options, "options");
        Policy<TraceDto> required = Policy.<TraceDto>alwaysOk()
                .and(requireNonBlank("TRACE_ID_REQUIRED", TraceDto::traceId, "traceId est requis"))
                .and(requireNonBlank("EVENT_CODE_REQUIRED", TraceDto::acteMetierCode, "acteMetierCode est requis"))
                .and(requireNonNull("TIMESTAMP_REQUIRED", TraceDto::createdOn, "createdOn est requis"));
        Policy<TraceDto> dbConstraints = Policy.<TraceDto>alwaysOk()
                .and(requireMaxLength("TRACE_ID_TOO_LONG", TraceDto::traceId, 80, "traceId max 80"))
                .and(requireMaxLength("EVENT_CODE_TOO_LONG", TraceDto::acteMetierCode, 120, "acteMetierCode max 120"));
        Policy<TraceDto> schemaAllowed = requireOneOf(
                "SCHEMA_NOT_ALLOWED",
                TraceDto::schemaVersion,
                options.allowedSchemas(),
                "schemaVersion non supporté"
        );
        return required
                .and(dbConstraints)
                .and(schemaAllowed);
    }

    private static boolean notTooFuture(Instant timestamp, Clock clock, Duration tolerance) {
        boolean isNotTooFuture = true;
        if ( timestamp != null ) {
            var now = Instant.now(clock);
            isNotTooFuture = !timestamp.isAfter(now.plus(tolerance));
        }
        return isNotTooFuture;
    }
}