package com.ocosur.ocosystem.livechicken.inboundbatch;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.livechicken.inboundbatch.dto.InboundBatchCreateRequestDTO;
import com.ocosur.ocosystem.livechicken.inboundbatch.dto.InboundBatchResponseDTO;
import com.ocosur.ocosystem.livechicken.inboundbatch.dto.InboundBatchUpdateRequestDTO;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/live-chicken/inbound-batches")
@AllArgsConstructor
public class InbountBatchController {

    private final InboundBatchService inboundBatchService;

    @GetMapping("/latest")
    public List<InboundBatchResponseDTO> getLatest(
            @RequestParam(defaultValue = "15") int limit) {
        return inboundBatchService.getLatest(limit).getContent();
    }

    @GetMapping("/{id}")
    public InboundBatchResponseDTO getById(@PathVariable Long id) {
        return inboundBatchService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public InboundBatchResponseDTO create(@RequestBody InboundBatchCreateRequestDTO inboundBatch) {
        return inboundBatchService.create(inboundBatch);
    }

    @PutMapping("/{id}")
    public InboundBatchResponseDTO update(
            @PathVariable Long id,
            @RequestBody InboundBatchUpdateRequestDTO inboundBatch) {
        return inboundBatchService.update(id, inboundBatch);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        inboundBatchService.delete(id);
    }
}
