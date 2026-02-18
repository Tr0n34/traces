package fr.cnamts.cpam33.traces.publisher.consumer.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(name = "ingested_trace")
public class TraceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "trace_id", nullable = false, length = 80)
    private Long traceId;

    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

    @Column(name = "bounded_context", nullable = false, length = 80)
    private String boundedContext;

    @Column(name = "acte_metier", nullable = false, length = 120)
    private String acteMetier;

    @Column(name = "trace_in", columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String traceIn;

    @Column(name = "trace_out", columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String traceOut;

    protected TraceEntity() {}

    public TraceEntity(Long traceId, Instant receivedAt, String boundedContext,
                       String acteMetier, String traceIn, String traceOut) {
        this.traceId = traceId;
        this.receivedAt = receivedAt;
        this.boundedContext = boundedContext;
        this.acteMetier = acteMetier;
        this.traceIn = traceIn;
        this.traceOut = traceOut;
    }

    public Long traceId() {
        return traceId;
    }

    public TraceEntity setTraceId(Long traceId) {
        this.traceId = traceId;
        return this;
    }

    public Instant receivedAt() {
        return receivedAt;
    }

    public TraceEntity setReceivedAt(Instant receivedAt) {
        this.receivedAt = receivedAt;
        return this;
    }

    public String boundedContext() {
        return boundedContext;
    }

    public TraceEntity setBoundedContext(String boundedContext) {
        this.boundedContext = boundedContext;
        return this;
    }

    public String acteMetier() {
        return acteMetier;
    }

    public TraceEntity setActeMetier(String acteMetier) {
        this.acteMetier = acteMetier;
        return this;
    }

    public String traceIn() {
        return traceIn;
    }

    public TraceEntity setTraceIn(String traceIn) {
        this.traceIn = traceIn;
        return this;
    }

    public String traceOut() {
        return traceOut;
    }

    public TraceEntity setTraceOut(String traceOut) {
        this.traceOut = traceOut;
        return this;
    }

}

