package fr.cnamts.cpam33.traces.publisher.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TraceConfiguration {

    @Bean("traceObjectMapper")
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

}
