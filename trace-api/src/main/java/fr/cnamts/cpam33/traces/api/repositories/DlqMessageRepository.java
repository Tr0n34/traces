package fr.cnamts.cpam33.traces.api.repositories;

import fr.cnamts.cpam33.traces.api.configurations.DlqStatus;
import fr.cnamts.cpam33.traces.api.entities.DlqMessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DlqMessageRepository extends JpaRepository<DlqMessageEntity, Long> {

    Page<DlqMessageEntity> findByStatus(DlqStatus status, Pageable pageable);

}
