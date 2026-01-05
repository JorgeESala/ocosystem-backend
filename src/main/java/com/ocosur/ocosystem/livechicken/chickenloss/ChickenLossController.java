package com.ocosur.ocosystem.livechicken.chickenloss;

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

import com.ocosur.ocosystem.livechicken.chickenloss.dto.ChickenLossCreateRequestDTO;
import com.ocosur.ocosystem.livechicken.chickenloss.dto.ChickenLossResponseDTO;
import com.ocosur.ocosystem.livechicken.chickenloss.dto.ChickenLossUpdateRequestDTO;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/live-chicken/chicken-losses")
@AllArgsConstructor
public class ChickenLossController {
    private final ChickenLossService chickenLossService;

    @GetMapping
    public List<ChickenLossResponseDTO> getAllChickenLosses() {
        return chickenLossService.getAllChickenLosses();
    }
    
    @GetMapping("/by-batch/{batchId}")
    public List<ChickenLossResponseDTO> getAllByBatch(@PathVariable Long batchId) {
        return chickenLossService.getAllByInboundBatchId(batchId);
    }

    @PostMapping()
    public ResponseEntity<ChickenLossResponseDTO> create(
            @RequestBody @Valid ChickenLossCreateRequestDTO dto) {
        ChickenLossResponseDTO created = chickenLossService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @PutMapping("/{id}")
    public ResponseEntity<ChickenLossResponseDTO> update(
            @PathVariable Long id,
            @RequestBody @Valid ChickenLossUpdateRequestDTO dto) {
        ChickenLossResponseDTO updated = chickenLossService.update(id, dto);

        return ResponseEntity.ok(updated);
    }
}
