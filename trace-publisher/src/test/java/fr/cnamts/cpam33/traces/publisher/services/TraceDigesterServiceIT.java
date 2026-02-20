package fr.cnamts.cpam33.traces.publisher.services;


import fr.cnamts.cpam33.traces.contract.dto.TraceDto;
import fr.cnamts.cpam33.traces.publisher.configurations.TraceDigesterConfiguration;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@SpringJUnitConfig(classes = TraceDigesterConfiguration.class)
class TraceDigesterServiceIT {

    @Autowired
    private TraceDigesterService service;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void ingest_should_publish() {
        TraceDto dto = mock(TraceDto.class);
        when(dto.acteMetierCode()).thenReturn("PATIENT_CREER");
        service.ingest(dto);
        verify(rabbitTemplate).convertAndSend(
                eq("trace.exchange"),
                eq("trace.PATIENT_CREER"),
                eq(dto),
                any(MessagePostProcessor.class)
        );
    }

    @Test
    void service_should_be_transactional_proxy_and_publish() {
        assertTrue(AopUtils.isAopProxy(service), "Service should be proxied to apply @Transactional");
        TraceDto dto = mock(TraceDto.class);
        when(dto.acteMetierCode()).thenReturn("PATIENT_CREER");
        var result = service.ingest(dto);
        verify(rabbitTemplate).convertAndSend(
                eq("trace.exchange"),
                eq("trace.PATIENT_CREER"),
                eq(dto),
                any(MessagePostProcessor.class)
        );
        assertFalse(result.isAlreadyDigested());
    }

}

