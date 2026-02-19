package fr.cnamts.cpam33.traces.publisher.api;

import fr.cnamts.cpam33.traces.contract.dto.TraceDto;
import fr.cnamts.cpam33.traces.publisher.services.TraceDigesterService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> ingest(@RequestBody @Valid TraceDto dto) {
        ResponseEntity<Void> responseEntity = ResponseEntity.ok().build();
        logger.trace("Received trace message: {}", dto.acteMetierCode());
        var res = traceDigesterService.ingest(dto);
        if ( !res.isAlreadyDigested()) {
            responseEntity = ResponseEntity.accepted()
                    .location(URI.create("/api/v1/traces/"))
                    .build();
        }
        return responseEntity;
    }
}
