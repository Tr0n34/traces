package fr.cnamts.cpam33.traces.api.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cnamts.cpam33.traces.api.configurations.DlqStatus;
import fr.cnamts.cpam33.traces.api.dto.DlqMessageDetailDto;
import fr.cnamts.cpam33.traces.api.dto.DlqMessageSummaryDto;
import fr.cnamts.cpam33.traces.api.entities.DlqMessageEntity;
import fr.cnamts.cpam33.traces.api.mappers.DlqMessageApiMapper;
import fr.cnamts.cpam33.traces.api.repositories.DlqMessageRepository;
import fr.cnamts.cpam33.traces.api.services.IDlqMessageService;
import jakarta.transaction.Transactional;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@Service
public class DlqMessageService implements IDlqMessageService {

    private final DlqMessageRepository dlqMessageRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private final DlqMessageApiMapper dlqMessageApiMapper;

    private final String defaultExchange;
    private final String defaultRoutingKey;
    private final boolean preferOriginal;

    public DlqMessageService(
            DlqMessageRepository dlqMessageRepository,
            RabbitTemplate rabbitTemplate,
            ObjectMapper objectMapper,
            DlqMessageApiMapper dlqMessageApiMapper,
            @Value("${dlq-admin.republish.default-exchange}") String defaultExchange,
            @Value("${dlq-admin.republish.default-routing-key}") String defaultRoutingKey,
            @Value("${dlq-admin.republish.prefer-original-from-x-death:true}") boolean preferOriginal
    ) {
        this.dlqMessageRepository = dlqMessageRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
        this.dlqMessageApiMapper = dlqMessageApiMapper;
        this.defaultExchange = defaultExchange;
        this.defaultRoutingKey = defaultRoutingKey;
        this.preferOriginal = preferOriginal;
    }

    @Override
    public Page<DlqMessageSummaryDto> list(DlqStatus status, Pageable pageable) {
        DlqStatus entityStatus = DlqStatus.valueOf(status.name());
        return dlqMessageRepository.findByStatus(entityStatus, pageable).map(dlqMessageApiMapper::toSummaryDto);
    }

    @Override
    public DlqMessageDetailDto get(Long id) {
        return dlqMessageApiMapper.toDetailDto(findOrThrow(id));
    }

    @Override
    @Transactional
    public DlqMessageDetailDto update(Long id, String payloadBase64, String headersJson) {
        DlqMessageEntity dlqMessageEntity = findOrThrow(id);
        if ( dlqMessageEntity.getStatus() != DlqStatus.PARKED ) {
            throw new IllegalStateException("Message not editable (status=" + dlqMessageEntity.getStatus() + ")");
        }
        readHeaders(headersJson);
        dlqMessageEntity.setPayload(Base64.getDecoder().decode(payloadBase64))
                .setHeadersJson(headersJson)
                .setUpdatedAt(Instant.now());
        return dlqMessageApiMapper.toDetailDto(dlqMessageRepository.save(dlqMessageEntity));
    }

    @Override
    @Transactional
    public void republish(Long id) {
        DlqMessageEntity dlqMessageEntity = findOrThrow(id);
        if ( dlqMessageEntity.getStatus() != DlqStatus.PARKED ) {
            throw new IllegalStateException("Message not republishable (status=" + dlqMessageEntity.getStatus() + ")");
        }
        String exchange = pickExchange(dlqMessageEntity);
        String routingKey = pickRoutingKey(dlqMessageEntity);
        Map<String, Object> headers = readHeaders(dlqMessageEntity.getHeadersJson());
        removeHeader(headers);
        MessageProperties messageProperties = loadMessageProperties(headers,  dlqMessageEntity.getContentType());
        Message message = new Message(dlqMessageEntity.getPayload(), messageProperties);
        try {
            rabbitTemplate.send(exchange, routingKey, message);
            dlqMessageEntity.setStatus(DlqStatus.REPUBLISHED);
            dlqMessageEntity.setLastRepublishAt(Instant.now());
            dlqMessageEntity.setRepublishAttempts(
                    Optional.ofNullable(dlqMessageEntity.getRepublishAttempts()).orElse(0) + 1
            );
            dlqMessageEntity.setLastError(null);
            dlqMessageRepository.save(dlqMessageEntity);
        } catch (Exception ex) {
            dlqMessageEntity.setLastError(ex.getMessage());
            dlqMessageEntity.setRepublishAttempts(Optional.ofNullable(dlqMessageEntity.getRepublishAttempts()).orElse(0) + 1);
            dlqMessageRepository.save(dlqMessageEntity);
            throw ex;
        }
    }

    @Override
    @Transactional
    public void discard(Long id) {
        DlqMessageEntity e = findOrThrow(id);
        e.setStatus(DlqStatus.DISCARDED);
        e.setUpdatedAt(Instant.now());
        dlqMessageRepository.save(e);
    }

    private MessageProperties loadMessageProperties(Map<String, Object> headers, String contentType) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setContentType(contentType);
        messageProperties.getHeaders().putAll(headers);
        return messageProperties;
    }

    private void removeHeader(Map<String, Object> headers) {
        headers.remove("x-death");
        headers.remove("x-first-death-exchange");
        headers.remove("x-first-death-routing-key");
    }

    private DlqMessageEntity findOrThrow(Long id) {
        return dlqMessageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("DLQ message not found: " + id));
    }

    private Map<String, Object> readHeaders(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid headersJson", e);
        }
    }

    private String pickExchange(DlqMessageEntity dlqMessageEntity) {
        String exchange = defaultExchange;
        if ( preferOriginal
                && dlqMessageEntity.getOriginalExchange() != null
                && !dlqMessageEntity.getOriginalExchange().isBlank() ) {
            exchange = dlqMessageEntity.getOriginalExchange();
        }
        return exchange;
    }

    private String pickRoutingKey(DlqMessageEntity dlqMessageEntity) {
        String routingKey = defaultRoutingKey;
        if ( preferOriginal
                && dlqMessageEntity.getOriginalRoutingKey() != null
                && !dlqMessageEntity.getOriginalRoutingKey().isBlank() ) {
            routingKey = dlqMessageEntity.getOriginalRoutingKey();
        }
        return routingKey;
    }

}
