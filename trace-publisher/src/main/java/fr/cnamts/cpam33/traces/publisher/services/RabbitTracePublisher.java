package fr.cnamts.cpam33.traces.publisher.services;

import fr.cnamts.cpam33.traces.contract.dto.TraceDto;
import fr.cnamts.cpam33.traces.publisher.configurations.TraceRabbitProperties;
import fr.cnamts.cpam33.traces.publisher.configurations.RabbitMessageHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitTracePublisher {

    private static final Logger logger = LoggerFactory.getLogger(RabbitTracePublisher.class);

    private final RabbitTemplate rabbitTemplate;
    private final TraceRabbitProperties traceRabbitProperties;

    public RabbitTracePublisher(RabbitTemplate rabbitTemplate, TraceRabbitProperties traceRabbitProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.traceRabbitProperties = traceRabbitProperties;
    }

    public void publish(TraceDto traceDto) {
        String rk = String.format(
                traceRabbitProperties.routingPattern(),
                traceDto.acteMetierCode()
        );
        MessagePostProcessor headers = message -> {
            message.getMessageProperties().setHeader(RabbitMessageHeader.SCHEMA_VERSION.getName(), traceDto.schemaVersion());
            message.getMessageProperties().setHeader(RabbitMessageHeader.ACTE_METIER_CODE.getName(), traceDto.acteMetierCode());
            message.getMessageProperties().setHeader(RabbitMessageHeader.UTILISATEUR_ID.getName(), traceDto.utilisateurId());
            message.getMessageProperties().setHeader(RabbitMessageHeader.TIMESTAMP.getName(), traceDto.timestamp().toString());
            return message;
        };
        logger.trace("Publishing trace message: {}", traceDto);
        rabbitTemplate.convertAndSend(traceRabbitProperties.exchange(), rk, traceDto, headers);
    }

}
