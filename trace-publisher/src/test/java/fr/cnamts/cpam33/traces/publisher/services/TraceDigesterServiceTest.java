package fr.cnamts.cpam33.traces.publisher.services;

import fr.cnamts.cpam33.traces.contract.dto.TraceDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TraceDigesterServiceTest {

    @Test
    void ingest_should_publish_and_return_not_already_digested() {
        RabbitTracePublisher publisher = mock(RabbitTracePublisher.class);
        TraceDigesterService service = new TraceDigesterService(publisher);
        TraceDto dto = mock(TraceDto.class);
        TraceDigesterService.Result result = service.ingest(dto);
        verify(publisher).publish(dto);
        verifyNoMoreInteractions(publisher);
        assertNotNull(result);
        assertFalse(result.isAlreadyDigested());
    }

}

