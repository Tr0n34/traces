package fr.cnamts.cpam33.traces.api;

import fr.cnamts.cpam33.traces.dto.TraceDto;
import fr.cnamts.cpam33.traces.services.TraceDigesterService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/traces")
public class TraceController {

    private final TraceDigesterService traceDigesterService;

    public TraceController(TraceDigesterService traceDigesterService) {
        this.traceDigesterService = traceDigesterService;
    }

    @PostMapping
    public ResponseEntity<Void> ingest(@RequestBody @Valid TraceDto dto) {
        ResponseEntity<Void> responseEntity = ResponseEntity.ok().build();
        var res = traceDigesterService.ingest(dto);
        if ( !res.isAlreadyDigested()) {
            responseEntity = ResponseEntity.accepted()
                    .location(URI.create("/api/v1/traces/" + dto.traceId()))
                    .build();
        }
        return responseEntity;
    }
}
