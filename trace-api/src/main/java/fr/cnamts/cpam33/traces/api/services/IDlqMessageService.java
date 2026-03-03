package fr.cnamts.cpam33.traces.api.services;

import fr.cnamts.cpam33.traces.api.configurations.DlqStatus;
import fr.cnamts.cpam33.traces.api.dto.DlqMessageDetailDto;
import fr.cnamts.cpam33.traces.api.dto.DlqMessageSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDlqMessageService {


    Page<DlqMessageSummaryDto> list(DlqStatus status, Pageable pageable);

    DlqMessageDetailDto get(Long id);

    DlqMessageDetailDto update(Long id, String payloadBase64, String headersJson);

    void republish(Long id);

    void discard(Long id);

}
