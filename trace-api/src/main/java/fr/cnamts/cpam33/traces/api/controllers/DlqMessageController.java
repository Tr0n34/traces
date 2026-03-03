package fr.cnamts.cpam33.traces.api.controllers;

import fr.cnamts.cpam33.traces.api.configurations.DlqStatus;
import fr.cnamts.cpam33.traces.api.dto.DlqMessageDetailDto;
import fr.cnamts.cpam33.traces.api.dto.DlqMessageSummaryDto;
import fr.cnamts.cpam33.traces.api.dto.UpdateDlqMessageRequest;
import fr.cnamts.cpam33.traces.api.services.IDlqMessageService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/dlq/messages")
public class DlqMessageController {

    private final IDlqMessageService dlqMessageService;

    public DlqMessageController(IDlqMessageService dlqMessageService) {
        this.dlqMessageService = dlqMessageService;
    }

    @GetMapping
    public Page<DlqMessageSummaryDto> list(
            @RequestParam(defaultValue = "PARKED") DlqStatus status,
            Pageable pageable
    ) {
        return dlqMessageService.list(status, pageable);
    }

    @GetMapping("/{id}")
    public DlqMessageDetailDto get(@PathVariable Long id) {
        return dlqMessageService.get(id);
    }

    @PutMapping("/{id}")
    public DlqMessageDetailDto update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateDlqMessageRequest updateDlqMessageRequest
    ) {
        return dlqMessageService.update(
                id,
                updateDlqMessageRequest.payloadBase64(),
                updateDlqMessageRequest.headersJson()
        );
    }

    @PostMapping("/{id}/republish")
    public void republish(@PathVariable Long id) {
        dlqMessageService.republish(id);
    }

    @PostMapping("/{id}/discard")
    public void discard(@PathVariable Long id) {
        dlqMessageService.discard(id);
    }

}
