package fr.cnamts.cpam33.traces.publisher.repositories;

import fr.cnamts.cpam33.traces.publisher.entities.TraceEntity;
import fr.cnamts.cpam33.traces.publisher.configurations.TraceStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface TraceJpaRepository extends JpaRepository<TraceEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select t
        from TraceOutboxEntity t
        where t.status = :status
          and (t.nextRetryAt is null or t.nextRetryAt <= :now)
        order by t.createdAt asc
        """)
    List<TraceEntity> findRetryableLocked(
            @Param("status") TraceStatus status,
            @Param("now") OffsetDateTime now,
            org.springframework.data.domain.Pageable pageable
    );

    default List<TraceEntity> findRetryable(OffsetDateTime now, int limit) {
        return findRetryableLocked(TraceStatus.RETRYING, now, org.springframework.data.domain.PageRequest.of(0, limit));
    }

}
