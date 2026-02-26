package fr.cnamts.cpam33.traces.publisher.services;

import fr.cnamts.cpam33.traces.contract.dto.TraceDto;
import fr.cnamts.cpam33.traces.publisher.configurations.RabbitMessageHeader;
import fr.cnamts.cpam33.traces.publisher.configurations.properties.TraceRabbitProperties;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RabbitTracePublisherTest {

    @Test
    void publish_should_build_routing_key_set_headers_and_send_message() {
        RabbitTemplate rabbitTemplate = mock(RabbitTemplate.class);
        TraceRabbitProperties props = mock(TraceRabbitProperties.class);
        when(props.exchange()).thenReturn("trace.exchange");
        when(props.routingPattern()).thenReturn("trace.%s");
        RabbitTracePublisher publisher = new RabbitTracePublisher(rabbitTemplate, props);
        TraceDto dto = mock(TraceDto.class);
        when(dto.acteMetierCode()).thenReturn("PATIENT_CREER");
        when(dto.schemaVersion()).thenReturn("1");
        when(dto.utilisateurId()).thenReturn("123456");
        OffsetDateTime ts = OffsetDateTime.parse("2026-02-19T20:10:00+01:00");
        when(dto.createdOn()).thenReturn(ts.toInstant());
        var mppCaptor = org.mockito.ArgumentCaptor.forClass(MessagePostProcessor.class);
        publisher.publish(dto);
        verify(rabbitTemplate).convertAndSend(
                eq("trace.exchange"),
                eq("trace.PATIENT_CREER"),
                eq(dto),
                mppCaptor.capture()
        );
        verifyNoMoreInteractions(rabbitTemplate);
        MessageProperties mp = new MessageProperties();
        Message msg = new Message(new byte[0], mp);
        Message processed = mppCaptor.getValue().postProcessMessage(msg);
        assertEquals("1", processed.getMessageProperties().getHeaders().get(RabbitMessageHeader.SCHEMA_VERSION.getName()));
        assertEquals("PATIENT_CREER", processed.getMessageProperties().getHeaders().get(RabbitMessageHeader.ACTE_METIER_CODE.getName()));
        assertEquals("123456", processed.getMessageProperties().getHeaders().get(RabbitMessageHeader.UTILISATEUR_ID.getName()));
    }

}
