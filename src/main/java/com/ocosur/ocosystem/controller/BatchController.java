package com.ocosur.ocosystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.dto.BatchItemResponseDTO;
import com.ocosur.ocosystem.dto.BatchRequestDTO;
import com.ocosur.ocosystem.dto.BatchSearchRequestDTO;
import com.ocosur.ocosystem.dto.BatchUpdateDTO;
import com.ocosur.ocosystem.model.Batch;
import com.ocosur.ocosystem.security.RequireBusiness;
import com.ocosur.ocosystem.service.BatchService;

@RestController
@CrossOrigin
@RequestMapping("/api/batches")
@RequireBusiness("BRANCHES")
public class BatchController {
    @Autowired
    BatchService batchService;

    @GetMapping()
    public ResponseEntity<List<Batch>> getBatches() {
        return new ResponseEntity<List<Batch>>(batchService.getBatches(), HttpStatus.OK);
    }

    @GetMapping("/{branchId}/latest")
    public ResponseEntity<List<Batch>> getLatestBatchesByBranch(@PathVariable Long branchId) {
        return new ResponseEntity<List<Batch>>(batchService.getLast4BatchesByBranch(branchId), HttpStatus.OK);
    }

    @GetMapping("/latest")
    public List<BatchItemResponseDTO> getLatestBatches() {
        return batchService.getLatestBatches();
    }

    @PostMapping("/search")
    public List<BatchItemResponseDTO> searchBatches(@RequestBody BatchSearchRequestDTO dto) {
        return batchService.searchByBranchAndDateRange(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Batch> updateBatch(
            @PathVariable Long id,
            @RequestBody BatchUpdateDTO dto) {
        Batch updated = batchService.updateBatch(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping()
    public Batch saveUser(@RequestBody BatchRequestDTO batch) {
        return batchService.createBatch(batch);
    }

}
