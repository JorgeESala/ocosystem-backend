package com.ocosur.ocosystem.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.core.employee.model.Employee;
import com.ocosur.ocosystem.core.employee.repository.EmployeeRepository;
import com.ocosur.ocosystem.dto.BatchSaleCreateRequestDTO;
import com.ocosur.ocosystem.dto.BatchSaleCreateResponseDTO;
import com.ocosur.ocosystem.dto.BatchSaleSearchRequestDTO;
import com.ocosur.ocosystem.dto.BatchSaleSearchResponseDTO;
import com.ocosur.ocosystem.mapper.BatchSaleMapper;
import com.ocosur.ocosystem.model.Batch;
import com.ocosur.ocosystem.model.BatchSale;
import com.ocosur.ocosystem.processed.client.Client;
import com.ocosur.ocosystem.processed.client.ProcessedClientRepository;
import com.ocosur.ocosystem.repository.BatchRepository;
import com.ocosur.ocosystem.repository.BatchSaleRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class BatchSaleService {
    private final BatchSaleRepository batchSaleRepository;
    private final BatchRepository batchRepository;
    private final EmployeeRepository employeeRepository;
    private final ProcessedClientRepository clientRepository;
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
    public BatchSaleCreateResponseDTO createBatchSale(BatchSaleCreateRequestDTO dto) {
        BatchSale sale = mapper.toEntity(dto);
        Batch batch = batchRepository.findById(dto.getBatchId())
                .orElseThrow(() -> new EntityNotFoundException("Batch not found"));

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        if (dto.getClientId() != null) {
            Client client = clientRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new EntityNotFoundException("Client not found"));

            sale.setClient(client);
        } else {
            // el usuario lo borró explícitamente
            sale.setClient(null);
        }

        sale.setBatch(batch);
        sale.setEmployee(employee);

        BatchSale saved = batchSaleRepository.save(sale);

        return mapper.toCreateResponse(saved);
    }

    @Transactional
    public BatchSaleCreateResponseDTO updateBatchSale(
            Long saleId,
            BatchSaleCreateRequestDTO dto) {
        BatchSale sale = batchSaleRepository.findById(saleId)
                .orElseThrow(() -> new EntityNotFoundException("Sale not found"));

        // ========= relaciones obligatorias =========

        Batch batch = batchRepository.findById(dto.getBatchId())
                .orElseThrow(() -> new EntityNotFoundException("Batch not found"));

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));

        sale.setBatch(batch);
        sale.setEmployee(employee);

        // ========= relación opcional =========

        if (dto.getClientId() != null) {
            Client client = clientRepository.findById(dto.getClientId())
                    .orElseThrow(() -> new EntityNotFoundException("Client not found"));

            sale.setClient(client);
        } else {
            // borrar cliente
            sale.setClient(null);
        }

        // ========= campos simples =========

        sale.setDate(dto.getDate());
        sale.setQuantitySold(dto.getQuantitySold());
        sale.setKgTotal(dto.getKgTotal());
        sale.setKgGut(dto.getKgGut());
        sale.setSaleTotal(dto.getSaleTotal());

        BatchSale updated = batchSaleRepository.save(sale);

        return mapper.toCreateResponse(updated);
    }

    public void save(BatchSale batchSale) {
        batchSaleRepository.save(batchSale);
    }

    public BatchSale findById(Long id) {
        return batchSaleRepository.findById(id).orElseThrow(() -> new RuntimeException("BatchSale not found"));
    }

    public List<BatchSale> findWithBatchBetweenDatesAndBranchIds(
            LocalDate start,
            LocalDate end,
            List<Long> branchIds) {
        return batchSaleRepository.findWithBatchBetweenDatesAndBranchIds(start, end, branchIds);
    }

    @Transactional
    public BatchSaleSearchResponseDTO search(BatchSaleSearchRequestDTO request) {

        List<BatchSale> sales = batchSaleRepository.findWithBatchBetweenDatesAndBranchIds(
                request.getStartDate(),
                request.getEndDate(),
                request.getBranchIds());

        var items = sales.stream()
                .map(mapper::toItemResponse)
                .toList();

        BigDecimal totalSales = sales.stream()
                .map(BatchSale::getSaleTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalKg = sales.stream()
                .map(BatchSale::getKgTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new BatchSaleSearchResponseDTO(
                items,
                totalSales,
                totalKg,
                (long) items.size());
    }

}
