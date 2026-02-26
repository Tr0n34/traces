package fr.cnamts.cpam33.traces.publisher.configurations;

import fr.cnamts.cpam33.traces.contract.policies.TracePolicyOptions;
import fr.cnamts.cpam33.traces.publisher.configurations.properties.TracePolicyProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(TracePolicyProperties.class)
public class TracePolicyConfiguration {

    @Bean
    public TracePolicyOptions tracePolicyOptions(TracePolicyProperties props) {
        return new TracePolicyOptions(
                props.allowedSchemas(),
                props.futureTolerance()
        );
    }

}
