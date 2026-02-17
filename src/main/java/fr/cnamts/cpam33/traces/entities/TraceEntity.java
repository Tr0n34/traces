package fr.cnamts.cpam33.traces.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.time.OffsetDateTime;

@Entity
@Table(name = "ingested_trace")
public class TraceEntity {

    @Id
    @Column(name = "trace_id", nullable = false, length = 80)
    private String traceId;

    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

    @Column(name = "bounded_context", nullable = false, length = 80)
    private String boundedContext;

    @Column(name = "acte_metier", nullable = false, length = 120)
    private String acteMetier;

    protected TraceEntity() {}

    public TraceEntity(String traceId, Instant receivedAt, String boundedContext, String acteMetier) {
        this.traceId = traceId;
        this.receivedAt = receivedAt;
        this.boundedContext = boundedContext;
        this.acteMetier = acteMetier;
    }

    public String getTraceId() { return traceId; }
}

