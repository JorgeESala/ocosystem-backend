package com.ocosur.ocosystem.service;

import java.util.List;
import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.dto.BatchSaleCreateDTO;
import com.ocosur.ocosystem.dto.BatchSaleResponseDTO;
import com.ocosur.ocosystem.mapper.BatchSaleMapper;
import com.ocosur.ocosystem.model.Batch;
import com.ocosur.ocosystem.model.BatchSale;
import com.ocosur.ocosystem.model.Employee;
import com.ocosur.ocosystem.repository.BatchSaleRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BatchSaleService {
    private final BatchSaleRepository batchSaleRepository;
    @PersistenceContext
    private EntityManager entityManager;
    private final BatchSaleMapper mapper;

    public List<BatchSale> getBatchSales() {
        return batchSaleRepository.findAll();
    }

    public List<BatchSale> getBatchSalesByBatchId(Long batchId) {
        return batchSaleRepository.findByBatchId(batchId);
    }

    @Transactional
    public BatchSaleResponseDTO createBatchSale(BatchSaleCreateDTO dto) {

        Batch batch = entityManager.getReference(
                Batch.class,
                dto.getBatchId());

        Employee employee = entityManager.getReference(
                Employee.class,
                dto.getEmployeeId());

        BatchSale entity = mapper.toEntity(dto, batch, employee);

        return mapper.toResponse(
                batchSaleRepository.save(entity));
    }

    public BatchSale updateBatchSale(BatchSale batchSale) {
        return batchSaleRepository.save(batchSale);
    }

    public void save(BatchSale batchSale) {
        batchSaleRepository.save(batchSale);
    }

    public BatchSale findById(Long id) {
        return batchSaleRepository.findById(id).orElseThrow(() -> new RuntimeException("BatchSale not found"));
    }

    public List<BatchSale> findWithBatchBetweenDatesAndBranchIds(
            java.time.LocalDate start,
            java.time.LocalDate end,
            List<Long> branchIds) {
        return batchSaleRepository.findWithBatchBetweenDatesAndBranchIds(start, end, branchIds);
    }

}
