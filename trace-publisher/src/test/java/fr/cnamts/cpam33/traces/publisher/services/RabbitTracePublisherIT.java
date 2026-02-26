package fr.cnamts.cpam33.traces.publisher.services;

import fr.cnamts.cpam33.traces.contract.dto.TraceDto;
import fr.cnamts.cpam33.traces.publisher.configurations.TraceDigesterTestConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        TraceDigesterTestConfiguration.class
})
class RabbitTracePublisherIT {

    @Autowired
    @Qualifier("rabbitTracePublisher")
    private RabbitTracePublisher publisher;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void publish_should_send_message_using_spring_wiring() {
        TraceDto dto = mock(TraceDto.class);
        when(dto.acteMetierCode()).thenReturn("PATIENT_CREER");
        when(dto.schemaVersion()).thenReturn("1");
        when(dto.utilisateurId()).thenReturn("123456");
        when(dto.createdOn()).thenReturn(OffsetDateTime.parse("2026-02-19T20:10:00+01:00").toInstant());
        var mppCaptor = org.mockito.ArgumentCaptor.forClass(MessagePostProcessor.class);
        publisher.publish(dto);
        verify(rabbitTemplate).convertAndSend(
                eq("trace.exchange"),
                eq("trace.PATIENT_CREER"),
                eq(dto),
                mppCaptor.capture()
        );
        var msg = new org.springframework.amqp.core.Message(new byte[0], new org.springframework.amqp.core.MessageProperties());
        var processed = mppCaptor.getValue().postProcessMessage(msg);
        assertEquals("PATIENT_CREER", processed.getMessageProperties().getHeaders().get("acteMetierCode"));
    }

}
