package com.ocosur.ocosystem.livechicken.batchsale;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.core.employee.model.Employee;
import com.ocosur.ocosystem.core.employee.repository.EmployeeRepository;
import com.ocosur.ocosystem.livechicken.batchsale.dto.InboundBatchSaleCreateRequestDTO;
import com.ocosur.ocosystem.livechicken.batchsale.dto.InboundBatchSaleResponseDTO;
import com.ocosur.ocosystem.livechicken.batchsale.dto.InboundBatchSaleUpdateRequestDTO;
import com.ocosur.ocosystem.livechicken.batchsale.mapper.InboundBatchSaleMapper;
import com.ocosur.ocosystem.livechicken.inboundbatch.InboundBatch;
import com.ocosur.ocosystem.livechicken.inboundbatch.InboundBatchRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class InboundBatchSaleService {
    private final InboundBatchSaleRepository inboundBatchSaleRepository;
    private final InboundBatchRepository inboundBatchRepository;
    private final EmployeeRepository employeeRepository;

    @Transactional
    public Page<InboundBatchSaleResponseDTO> getLatest(Pageable pageable) {
        return inboundBatchSaleRepository
                .findAllByOrderByDateDesc(pageable)
                .map(InboundBatchSaleMapper::toResponseDTO);
    }
    @Transactional
    public List<InboundBatchSaleResponseDTO> getAllByInboundBatchId(Long inboundBatchId) {
        return inboundBatchSaleRepository
                .findAllByBatch_IdOrderByDateDesc(inboundBatchId)
                .stream()
                .map(InboundBatchSaleMapper::toResponseDTO)
                .toList();
    }


    public InboundBatchSaleResponseDTO create(
            Long batchId,
            InboundBatchSaleCreateRequestDTO dto
    ) {
        InboundBatch batch = inboundBatchRepository.findById(batchId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "InboundBatch not found: " + batchId));

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Employee not found: " + dto.getEmployeeId()));

        InboundBatchSale sale = InboundBatchSale.builder()
                .batch(batch)
                .employee(employee)
                .quantitySold(dto.getQuantitySold())
                .kgSold(dto.getKgSold())
                .kgSent(dto.getKgSent())
                .saleTotal(dto.getSaleTotal())
                .date(dto.getDate())
                .build();

        InboundBatchSale saved = inboundBatchSaleRepository.save(sale);

        return InboundBatchSaleMapper.toResponseDTO(saved);
    }

    public InboundBatchSaleResponseDTO update(
            Long saleId,
            InboundBatchSaleUpdateRequestDTO dto
    ) {
        InboundBatchSale sale = inboundBatchSaleRepository
                .findById(saleId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sale not found: " + saleId));

        if (dto.getEmployeeId() != null) {
            Employee employee = employeeRepository.findById(dto.getEmployeeId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Employee not found: " + dto.getEmployeeId()));
            sale.setEmployee(employee);
        }

        sale.setQuantitySold(dto.getQuantitySold());
        sale.setKgSold(dto.getKgSold());
        sale.setKgSent(dto.getKgSent());
        sale.setSaleTotal(dto.getSaleTotal());
        sale.setDate(dto.getDate());

        InboundBatchSale updated = inboundBatchSaleRepository.save(sale);

        return InboundBatchSaleMapper.toResponseDTO(updated);
    }
}
