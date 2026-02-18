package fr.cnamts.cpam33.traces.publisher.consumer.repositories;

import fr.cnamts.cpam33.traces.publisher.consumer.entities.TraceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraceRepository extends JpaRepository<TraceEntity, Long> {

}
