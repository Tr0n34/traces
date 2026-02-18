package fr.cnamts.cpam33.traces.publisher.api;

import fr.cnamts.cpam33.traces.publisher.dto.TraceDto;
import fr.cnamts.cpam33.traces.publisher.services.TraceDigesterService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/traces")
public class TraceController {

    private static final Logger logger = LoggerFactory.getLogger(TraceController.class);

    private final TraceDigesterService traceDigesterService;

    public TraceController(TraceDigesterService traceDigesterService) {
        this.traceDigesterService = traceDigesterService;
    }

    @PostMapping
    public ResponseEntity<Void> ingest(@RequestBody @Valid TraceDto dto) {
        ResponseEntity<Void> responseEntity = ResponseEntity.ok().build();
        logger.trace("trace_to_ingest={}", dto.toString());
        var res = traceDigesterService.ingest(dto);
        if ( !res.isAlreadyDigested()) {
            responseEntity = ResponseEntity.accepted()
                    .location(URI.create("/api/v1/traces/"))
                    .build();
        }
        return responseEntity;
    }
}
