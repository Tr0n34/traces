package fr.cnamts.cpam33.traces.publisher.consumer.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class ClockConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

}
