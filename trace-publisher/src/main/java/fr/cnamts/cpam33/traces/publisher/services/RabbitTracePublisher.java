package fr.cnamts.cpam33.traces.publisher.services;

import fr.cnamts.cpam33.traces.contract.dto.TraceDto;
import fr.cnamts.cpam33.traces.publisher.configurations.TraceRabbitProperties;
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
            message.getMessageProperties().setHeader("schemaVersion", traceDto.schemaVersion());
            message.getMessageProperties().setHeader("acteMetierCode", traceDto.acteMetierCode());
            message.getMessageProperties().setHeader("utilisateurId", traceDto.utilisateurId());
            message.getMessageProperties().setHeader("timestamp", traceDto.timestamp().toString());
            return message;
        };
        logger.trace("Publishing trace message: {}", traceDto);
        rabbitTemplate.convertAndSend(traceRabbitProperties.exchange(), rk, traceDto, headers);
    }

}
