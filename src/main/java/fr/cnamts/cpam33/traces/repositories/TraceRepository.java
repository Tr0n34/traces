package fr.cnamts.cpam33.traces.repositories;

import fr.cnamts.cpam33.traces.entities.TraceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraceRepository extends JpaRepository<TraceEntity, String> {

}
