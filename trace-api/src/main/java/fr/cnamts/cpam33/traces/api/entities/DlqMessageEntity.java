package fr.cnamts.cpam33.traces.api.entities;

import fr.cnamts.cpam33.traces.api.configurations.DlqStatus;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "dlq_message")
public class DlqMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_queue", nullable = false)
    private String sourceQueue;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private DlqStatus status;

    @Column(name = "parked_at", nullable = false)
    private Instant parkedAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "original_exchange")
    private String originalExchange;

    @Column(name = "original_routing_key")
    private String originalRoutingKey;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Lob
    @Column(name = "payload", nullable = false)
    private byte[] payload;

    @Lob
    @Column(name = "headers_json", nullable = false)
    private String headersJson;

    @Column(name = "last_error")
    private String lastError;

    @Column(name = "republish_attempts")
    private Integer republishAttempts;

    @Column(name = "last_republish_at")
    private Instant lastRepublishAt;

    public Long getId() {
        return id;
    }

    public DlqMessageEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getSourceQueue() {
        return sourceQueue;
    }

    public DlqMessageEntity setSourceQueue(String sourceQueue) {
        this.sourceQueue = sourceQueue;
        return this;
    }

    public DlqStatus getStatus() {
        return status;
    }

    public DlqMessageEntity setStatus(DlqStatus status) {
        this.status = status;
        return this;
    }

    public Instant getParkedAt() {
        return parkedAt;
    }

    public DlqMessageEntity setParkedAt(Instant parkedAt) {
        this.parkedAt = parkedAt;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public DlqMessageEntity setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }

    public String getOriginalExchange() {
        return originalExchange;
    }

    public DlqMessageEntity setOriginalExchange(String originalExchange) {
        this.originalExchange = originalExchange;
        return this;
    }

    public String getOriginalRoutingKey() {
        return originalRoutingKey;
    }

    public DlqMessageEntity setOriginalRoutingKey(String originalRoutingKey) {
        this.originalRoutingKey = originalRoutingKey;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public DlqMessageEntity setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public byte[] getPayload() {
        return payload;
    }

    public DlqMessageEntity setPayload(byte[] payload) {
        this.payload = payload;
        return this;
    }

    public String getHeadersJson() {
        return headersJson;
    }

    public DlqMessageEntity setHeadersJson(String headersJson) {
        this.headersJson = headersJson;
        return this;
    }

    public String getLastError() {
        return lastError;
    }

    public DlqMessageEntity setLastError(String lastError) {
        this.lastError = lastError;
        return this;
    }

    public Integer getRepublishAttempts() {
        return republishAttempts;
    }

    public DlqMessageEntity setRepublishAttempts(Integer republishAttempts) {
        this.republishAttempts = republishAttempts;
        return this;
    }

    public Instant getLastRepublishAt() {
        return lastRepublishAt;
    }

    public DlqMessageEntity setLastRepublishAt(Instant lastRepublishAt) {
        this.lastRepublishAt = lastRepublishAt;
        return this;
    }

}
