package fr.cnamts.cpam33.traces.consumer.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;

@Entity
@Table(name = "ingested_trace")
public class TraceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "trace_id", nullable = false, length = 80)
    private String traceId;

    @Column(name = "application_id", nullable = false, length = 120)
    private String applicationId;

    @Column(name = "utilisateur_id", nullable = false)
    private String utilisateurId;

    @Column(name = "utilisateur-ip", nullable = false)
    private String utilisateurIp;

    @Column(name = "correlation_id", nullable = false)
    private String correlationId;

    @Column(name = "ecran", nullable = false)
    private String frontPage;

    @Column(name = "bounded_context", nullable = false, length = 80)
    private String boundedContext;

    @Column(name = "fonction", nullable = false)
    private String fonction;

    @Column(name = "acte_metier", nullable = false, length = 120)
    private String acteMetier;

    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

    @Column(name = "created_on")
    private Instant createdOn;

    @Column(name = "trace_in", columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String traceIn;

    @Column(name = "trace_out", columnDefinition = "jsonb", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    private String traceOut;

    public TraceEntity() {}

    public TraceEntity(
            String traceId,
            String applicationId,
            String utilisateurId,
            String utilisateurIp,
            String correlationId,
            String frontPage,
            String boundedContext,
            String fonction,
            String acteMetier,
            Instant receivedAt,
            Instant createdOn,
            String traceIn,
            String traceOut) {
        this.traceId = traceId;
        this.applicationId = applicationId;
        this.utilisateurId = utilisateurId;
        this.utilisateurIp = utilisateurIp;
        this.correlationId = correlationId;
        this.frontPage = frontPage;
        this.boundedContext = boundedContext;
        this.fonction = fonction;
        this.acteMetier = acteMetier;
        this.receivedAt = receivedAt;
        this.createdOn = createdOn;
        this.traceIn = traceIn;
        this.traceOut = traceOut;
    }

    public Long id() {
        return id;
    }

    public TraceEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String traceId() {
        return traceId;
    }

    public TraceEntity setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public String applicationId() {
        return applicationId;
    }

    public TraceEntity setApplicationId(String applicationId) {
        this.applicationId = applicationId;
        return this;
    }

    public String utilisateurId() {
        return utilisateurId;
    }

    public TraceEntity setUtilisateurId(String utilisateurId) {
        this.utilisateurId = utilisateurId;
        return this;
    }

    public String utilisateurIp() {
        return utilisateurIp;
    }

    public TraceEntity setUtilisateurIp(String utilisateurIp) {
        this.utilisateurIp = utilisateurIp;
        return this;
    }

    public String correlationId() {
        return correlationId;
    }

    public TraceEntity setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    public String frontPage() {
        return frontPage;
    }

    public TraceEntity setFrontPage(String frontPage) {
        this.frontPage = frontPage;
        return this;
    }

    public String boundedContext() {
        return boundedContext;
    }

    public TraceEntity setBoundedContext(String boundedContext) {
        this.boundedContext = boundedContext;
        return this;
    }

    public String fonction() {
        return fonction;
    }

    public TraceEntity setFonction(String fonction) {
        this.fonction = fonction;
        return this;
    }

    public String acteMetier() {
        return acteMetier;
    }

    public TraceEntity setActeMetier(String acteMetier) {
        this.acteMetier = acteMetier;
        return this;
    }

    public Instant receivedAt() {
        return receivedAt;
    }

    public TraceEntity setReceivedAt(Instant receivedAt) {
        this.receivedAt = receivedAt;
        return this;
    }

    public Instant createdOn() {
        return createdOn;
    }

    public TraceEntity setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
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

