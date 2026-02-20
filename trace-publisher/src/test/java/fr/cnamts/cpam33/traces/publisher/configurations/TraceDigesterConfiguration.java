package fr.cnamts.cpam33.traces.publisher.configurations;

import fr.cnamts.cpam33.traces.publisher.services.RabbitTracePublisher;
import fr.cnamts.cpam33.traces.publisher.services.TraceDigesterService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.SimpleTransactionStatus;

import java.util.List;

import static org.mockito.Mockito.mock;

@Configuration
@EnableTransactionManagement
public class TraceDigesterConfiguration {

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return mock(RabbitTemplate.class);
    }

    @Bean
    public TraceRabbitProperties traceRabbitProperties() {
        return new TraceRabbitProperties(
                "trace.exchange",
                List.of("trace.queue.1", "trace.queue.2"),
                "trace.dlq",
                "trace.%s",
                "trace.%s"
        );
    }

    @Bean
    public RabbitTracePublisher rabbitTracePublisher(RabbitTemplate rt,
                                                     TraceRabbitProperties props) {
        return new RabbitTracePublisher(rt, props);
    }

    @Bean
    public TraceDigesterService traceDigesterService(RabbitTracePublisher publisher) {
        return new TraceDigesterService(publisher);
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new PlatformTransactionManager() {
            @Override
            public TransactionStatus getTransaction(TransactionDefinition definition) {
                return new SimpleTransactionStatus();
            }

            @Override
            public void commit(TransactionStatus status) {}

            @Override
            public void rollback(TransactionStatus status) {}
        };
    }

}
