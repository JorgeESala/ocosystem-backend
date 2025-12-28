package com.ocosur.ocosystem.livechicken.batchsale;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.livechicken.batchsale.dto.InboundBatchSaleCreateRequestDTO;
import com.ocosur.ocosystem.livechicken.batchsale.dto.InboundBatchSaleResponseDTO;
import com.ocosur.ocosystem.livechicken.batchsale.dto.InboundBatchSaleUpdateRequestDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inbound-batches/{batchId}/sales")
@RequiredArgsConstructor
public class InboundBatchSaleController {

    private final InboundBatchSaleService inboundBatchSaleService;

    @GetMapping
    public List<InboundBatchSaleResponseDTO> getAllByBatch(@PathVariable Long batchId) {
        return inboundBatchSaleService.getAllByInboundBatchId(batchId);
    }

    @PostMapping
    public ResponseEntity<InboundBatchSaleResponseDTO> create(
            @PathVariable Long batchId,
            @RequestBody @Valid InboundBatchSaleCreateRequestDTO dto) {
        InboundBatchSaleResponseDTO created = inboundBatchSaleService.create(batchId, dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{saleId}")
    public ResponseEntity<InboundBatchSaleResponseDTO> update(
            @PathVariable Long saleId,
            @RequestBody @Valid InboundBatchSaleUpdateRequestDTO dto) {
        InboundBatchSaleResponseDTO updated = inboundBatchSaleService.update(saleId, dto);

        return ResponseEntity.ok(updated);
    }
}
