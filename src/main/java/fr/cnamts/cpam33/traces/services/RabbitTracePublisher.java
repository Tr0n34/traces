package fr.cnamts.cpam33.traces.services;

import fr.cnamts.cpam33.traces.configurations.TraceRabbitProperties;
import fr.cnamts.cpam33.traces.dto.TraceDto;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class RabbitTracePublisher {

    private final RabbitTemplate rabbitTemplate;
    private final TraceRabbitProperties traceRabbitProperties;

    public RabbitTracePublisher(RabbitTemplate rabbitTemplate, TraceRabbitProperties traceRabbitProperties) {
        this.rabbitTemplate = rabbitTemplate;
        this.traceRabbitProperties = traceRabbitProperties;
    }

    public void publish(TraceDto traceDto) {
        String rk = String.format(traceRabbitProperties.routingKeyTemplate(), traceDto.boundedContext(), traceDto.acteMetierCode());
        MessagePostProcessor headers = message -> {
            message.getMessageProperties().setMessageId(traceDto.traceId());
            message.getMessageProperties().setHeader("schemaVersion", traceDto.version());
            message.getMessageProperties().setHeader("boundedContext", traceDto.boundedContext());
            message.getMessageProperties().setHeader("acteMetierCode", traceDto.acteMetierCode());
            message.getMessageProperties().setHeader("utilisateurId", traceDto.utilisateurId());
            message.getMessageProperties().setHeader("timestamp", traceDto.timestamp().toString());
            return message;
        };
        rabbitTemplate.convertAndSend(traceRabbitProperties.exchange(), rk, traceDto, headers);
    }

}
