package fr.cnamts.cpam33.traces.publisher.services;

import fr.cnamts.cpam33.traces.contract.dto.TraceDto;
import fr.cnamts.cpam33.traces.contract.policies.PolicyResult;
import fr.cnamts.cpam33.traces.contract.policies.TracePolicy;
import fr.cnamts.cpam33.traces.contract.policies.TracePolicyOptions;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Duration;
import java.util.List;

@Service
public class TraceDigesterService {

    private static final Logger logger = LoggerFactory.getLogger(TraceDigesterService.class);

    private final Clock clock;
    private final RabbitTracePublisher rabbitTracePublisher;
    private final TracePolicyOptions tracePolicyOptions;

    public TraceDigesterService(RabbitTracePublisher rabbitTracePublisher,
                                TracePolicyOptions tracePolicyOptions,
                                Clock clock) {
        this.clock = clock;
        this.rabbitTracePublisher = rabbitTracePublisher;
        this.tracePolicyOptions = tracePolicyOptions;
    }

    @Transactional
    public void ingest(TraceDto traceDto, boolean activePolicy) {
        if ( activePolicy ) {
            PolicyResult result = TracePolicy.forPublish(clock).check(traceDto);
            if (!result.isOk()) {
                logger.warn("Trace invalide: {}", result.violations());
                throw result.toException("Trace invalide pour publication");
            }
        }
        rabbitTracePublisher.publish(traceDto);
    }

}
