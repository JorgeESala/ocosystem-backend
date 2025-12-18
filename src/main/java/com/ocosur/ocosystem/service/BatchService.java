package com.ocosur.ocosystem.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.dto.BatchItemResponseDTO;
import com.ocosur.ocosystem.dto.BatchRequestDTO;
import com.ocosur.ocosystem.dto.BatchSearchRequestDTO;
import com.ocosur.ocosystem.dto.BatchSearchResponseDTO;
import com.ocosur.ocosystem.dto.BatchUpdateDTO;
import com.ocosur.ocosystem.mapper.BatchMapper;
import com.ocosur.ocosystem.model.Batch;
import com.ocosur.ocosystem.model.Branch;
import com.ocosur.ocosystem.repository.BatchRepository;
import com.ocosur.ocosystem.repository.BranchRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BatchService {
    private final BatchRepository batchRepository;
    private final BranchRepository branchRepository;
    private final BatchMapper mapper;

    public List<Batch> getBatches() {
        return batchRepository.findAll();
    }

    public Batch saveBatch(Batch batch) {
        return batchRepository.save(batch);
    }

    @Transactional
    public Batch updateBatch(Long id, BatchUpdateDTO dto) {

        // 1. Buscar el Batch
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch no encontrado: " + id));

        // 2. Si viene branchId → validar y asignar
        if (dto.getBranchId() != null) {
            Branch branch = branchRepository.findById(dto.getBranchId())
                    .orElseThrow(() -> new RuntimeException("Sucursal no encontrada: " + dto.getBranchId()));
            batch.setBranch(branch);
        }

        // 3. Actualizar solo campos enviados (patch-like)
        if (dto.getKgTotal() != null) {
            batch.setKgTotal(dto.getKgTotal());
        }
        if (dto.getPricePerKg() != null) {
            batch.setPricePerKg(dto.getPricePerKg());
        }
        if (dto.getDate() != null) {
            batch.setDate(dto.getDate());
        }
        if (dto.getProvider() != null) {
            batch.setProvider(dto.getProvider());
        }
        if (dto.getChickenQuantity() != null) {
            batch.setChickenQuantity(dto.getChickenQuantity());
        }

        // 4. Save opcional, Hibernate hace dirty-checking
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

    public List<BatchItemResponseDTO> searchByBranchAndDateRange(BatchSearchRequestDTO request) {

        List<Batch> batches = batchRepository.findBetweenDatesAndBranchIds(
                request.startDate(),
                request.endDate(),
                request.branchIds());

        var items = batches.stream()
                .map(mapper::toItemResponse)
                .toList();

        return items;
    }

    public List<Batch> getLast4BatchesByBranch(Long branchId) {
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada"));

        Pageable limit = PageRequest.of(0, 4);
        return batchRepository.findByBranchOrderByDateDesc(branch, limit);
    }

    public Batch updateBatch(Batch batch) {
        return saveBatch(batch);
    }

    public Batch findById(Long id) {
        return batchRepository.findById(id).orElseThrow(() -> new RuntimeException("Batch not found"));
    }

    public List<Batch> findByBranchesAndDateRange(
            List<Long> branchIds,
            LocalDate start,
            LocalDate end) {
        return batchRepository.findByBranchIdInAndDateBetweenOrderByDateDesc(
                branchIds,
                start,
                end);
    }

    public List<BatchItemResponseDTO> getLatestBatches() {
        Pageable pageable = PageRequest.of(0, 15);
        List<Batch> batches = batchRepository.findAllByOrderByDateDesc(pageable).getContent();
        var items = batches.stream()
                .map(mapper::toItemResponse)
                .toList();
        return items;
    }

}
