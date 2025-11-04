package com.ocosur.ocosystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.dto.BatchRequestDTO;
import com.ocosur.ocosystem.model.Batch;
import com.ocosur.ocosystem.model.Branch;
import com.ocosur.ocosystem.repository.BatchRepository;
import com.ocosur.ocosystem.repository.BranchRepository;

@Service
public class BatchService {
    @Autowired
    public BatchRepository batchRepository;
    @Autowired
    public BranchRepository branchRepository;

    public List<Batch> getBatches() {
        return batchRepository.findAll();
    }

    public Batch saveBatch(Batch batch) {
        return batchRepository.save(batch);
    }

    public Batch createBatch(BatchRequestDTO request) {
        Branch branch = new Branch();
        branch.setId(request.getBranchId());

        Batch batch = new Batch();
        batch.setBranch(branch);
        batch.setProvider(request.getProvider());
        batch.setDate(request.getDate());
        batch.setChickenQuantity(request.getChickenQuantity());
        batch.setKgTotal(request.getKgTotal());
        batch.setPricePerKg(request.getPricePerKg());

        return batchRepository.save(batch);
    }

    public List<Batch> getLast4BatchesByBranch(Integer branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

        Pageable limit = PageRequest.of(0, 4);
        return batchRepository.findByBranchOrderByDateDesc(branch, limit);
    }
    public Batch updateBatch (Batch batch) {
        return saveBatch(batch);
    }

}
