package fr.cnamts.cpam33.traces.publisher.configurations.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.List;

@ConfigurationProperties(prefix = "traces.policy")
public record TracePolicyProperties(
        List<String> allowedSchemas,
        Duration futureTolerance
) {

    public TracePolicyProperties {
        if ( allowedSchemas == null || allowedSchemas.isEmpty() ) {
            throw new IllegalArgumentException("allowedSchemas must not be empty");
        }
        if ( futureTolerance == null ) {
            throw new IllegalArgumentException("futureTolerance must not be null");
        }
    }

}
