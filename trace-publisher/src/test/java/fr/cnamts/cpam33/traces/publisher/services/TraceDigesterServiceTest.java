package fr.cnamts.cpam33.traces.publisher.services;

import fr.cnamts.cpam33.traces.contract.dto.TraceDto;
import fr.cnamts.cpam33.traces.contract.dto.TraceInDto;
import fr.cnamts.cpam33.traces.contract.dto.TraceOutDto;
import fr.cnamts.cpam33.traces.contract.policies.TracePolicyOptions;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraceDigesterServiceTest {

    @Test
    void ingest_should_publish_and_return_not_already_digested() {
        TracePolicyOptions options = new TracePolicyOptions(
                List.of("1"),
                Duration.ofSeconds(10)
        );
        RabbitTracePublisher publisher = mock(RabbitTracePublisher.class);
        TraceDigesterService service = new TraceDigesterService(
                publisher,
                options,
                Clock.system(ZoneId.of("Europe/Paris"))
        );
        TraceDto dto = new TraceDto(
                "123456789",
                "1",
                "ORDONNANCE_SAGES2",
                "CREER_PATIENT",
                "CREER",
                "cor-id-12345679",
                "ecran_creer_patient",
                "PATIENT",
                "user123456798",
                "196.0.0.1",
                Instant.now(),
                mock(TraceInDto.class),
                mock(TraceOutDto.class)
        );
        service.ingest(dto, true);
        verify(publisher).publish(dto);
        verifyNoMoreInteractions(publisher);
    }

}

