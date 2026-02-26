package fr.cnamts.cpam33.traces.publisher.entities;

import com.fasterxml.jackson.databind.JsonNode;
import fr.cnamts.cpam33.traces.publisher.configurations.enums.TraceStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.OffsetDateTime;

@Entity
@Table(name = "trace_error")
public class TraceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trace_id", nullable = false, length = 80)
    private String traceId;

    @Column(name = "event_code", nullable = false, length = 120)
    private String eventCode;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload_json", columnDefinition = "jsonb", nullable = false)
    private JsonNode payloadJson;

    @Column(name = "reason")
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private TraceStatus status;

    @Column(name = "retry_count", nullable = false)
    private int retryCount;

    @Column(name = "next_retry_at", nullable = false)
    private OffsetDateTime nextRetryAt;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "last_failure_at")
    private OffsetDateTime lastFailureAt;

    @Column(name = "sent_at")
    private OffsetDateTime sentAt;

    public TraceEntity(Long id, String traceId,
                       String eventCode,
                       JsonNode payloadJson,
                       String reason,
                       TraceStatus status,
                       int retryCount,
                       OffsetDateTime nextRetryAt,
                       OffsetDateTime createdAt,
                       OffsetDateTime lastFailureAt,
                       OffsetDateTime sentAt) {

        this.id = id;
        this.traceId = traceId;
        this.eventCode = eventCode;
        this.payloadJson = payloadJson;
        this.reason = reason;
        this.status = status;
        this.retryCount = retryCount;
        this.nextRetryAt = nextRetryAt;
        this.createdAt = createdAt;
        this.lastFailureAt = lastFailureAt;
        this.sentAt = sentAt;
    }

    public Long getId() {
        return id;
    }

    public TraceEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getTraceId() {
        return traceId;
    }

    public TraceEntity setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public String getEventCode() {
        return eventCode;
    }

    public TraceEntity setEventCode(String eventCode) {
        this.eventCode = eventCode;
        return this;
    }

    public JsonNode getPayloadJson() {
        return payloadJson;
    }

    public TraceEntity setPayloadJson(JsonNode payloadJson) {
        this.payloadJson = payloadJson;
        return this;
    }

    public String getReason() {
        return reason;
    }

    public TraceEntity setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public TraceStatus getStatus() {
        return status;
    }

    public TraceEntity setStatus(TraceStatus status) {
        this.status = status;
        return this;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public TraceEntity setRetryCount(int retryCount) {
        this.retryCount = retryCount;
        return this;
    }

    public OffsetDateTime getNextRetryAt() {
        return nextRetryAt;
    }

    public TraceEntity setNextRetryAt(OffsetDateTime nextRetryAt) {
        this.nextRetryAt = nextRetryAt;
        return this;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public TraceEntity setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public OffsetDateTime getLastFailureAt() {
        return lastFailureAt;
    }

    public TraceEntity setLastFailureAt(OffsetDateTime lastFailureAt) {
        this.lastFailureAt = lastFailureAt;
        return this;
    }

    public OffsetDateTime getSentAt() {
        return sentAt;
    }

    public TraceEntity setSentAt(OffsetDateTime sentAt) {
        this.sentAt = sentAt;
        return this;
    }

}
