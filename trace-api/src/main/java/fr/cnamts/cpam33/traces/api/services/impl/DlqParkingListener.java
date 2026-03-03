package fr.cnamts.cpam33.traces.api.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import fr.cnamts.cpam33.traces.api.configurations.DlqStatus;
import fr.cnamts.cpam33.traces.api.dto.Route;
import fr.cnamts.cpam33.traces.api.entities.DlqMessageEntity;
import fr.cnamts.cpam33.traces.api.repositories.DlqMessageRepository;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class DlqParkingListener {

    private final DlqMessageRepository dlqMessageRepository;
    private final ObjectMapper objectMapper;
    private final String dlqQueue;

    public DlqParkingListener(
            DlqMessageRepository dlqMessageRepository,
            ObjectMapper objectMapper,
            @Value("${dlq-admin.dlq-queue}") String dlqQueue
    ) {
        this.dlqMessageRepository = dlqMessageRepository;
        this.objectMapper = objectMapper;
        this.dlqQueue = dlqQueue;
    }

    @RabbitListener(queues = "${dlq-admin.dlq-queue}")
    public void onDlqMessage(
            Message message,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long tag
    ) throws Exception {
        try {
            DlqMessageEntity entity = new DlqMessageEntity()
                    .setSourceQueue(dlqQueue)
                    .setStatus(DlqStatus.PARKED)
                    .setParkedAt(Instant.now())
                    .setContentType(
                            Optional.ofNullable(message.getMessageProperties().getContentType()).orElse("application/octet-stream")
                    )
                    .setPayload(message.getBody());
            Map<String, Object> headers = new HashMap<>(message.getMessageProperties().getHeaders());
            entity.setHeadersJson(objectMapper.writeValueAsString(headers));
            extractOriginalRoute(headers).ifPresent(route -> {
                entity.setOriginalExchange(route.exchange());
                entity.setOriginalRoutingKey(route.routingKey());
            });
            dlqMessageRepository.save(entity);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            // Pas d'ACK si park = KO
            channel.basicNack(tag, false, true);
            throw e;
        }
    }

    private Optional<Route> extractOriginalRoute(Map<String, Object> headers) {
        Object ex = headers.get("x-first-death-exchange");
        Object rk = headers.get("x-first-death-routing-key");
        return (ex instanceof String && rk instanceof String)
                ? Optional.of(new Route((String) ex, (String) rk))
                : Optional.empty();
    }

}
