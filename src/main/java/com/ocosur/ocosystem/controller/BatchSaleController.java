package com.ocosur.ocosystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.core.employee.model.Employee;
import com.ocosur.ocosystem.core.employee.repository.EmployeeRepository;
import com.ocosur.ocosystem.dto.BatchSaleCreateRequestDTO;
import com.ocosur.ocosystem.dto.BatchSaleCreateResponseDTO;
import com.ocosur.ocosystem.dto.BatchSaleSearchRequestDTO;
import com.ocosur.ocosystem.dto.BatchSaleSearchResponseDTO;
import com.ocosur.ocosystem.dto.BatchSaleUpdateDTO;
import com.ocosur.ocosystem.model.Batch;
import com.ocosur.ocosystem.model.BatchSale;
import com.ocosur.ocosystem.service.BatchSaleService;
import com.ocosur.ocosystem.service.BatchService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@CrossOrigin
@RequestMapping("/api/batchSales")
public class BatchSaleController {

    @Autowired
    BatchSaleService batchSaleService;
    @Autowired
    BatchService batchService;
    @Autowired
    EmployeeRepository employeeService;

    @GetMapping()
    public ResponseEntity<List<BatchSale>> getBatchSales() {
        return new ResponseEntity<List<BatchSale>>(batchSaleService.getBatchSales(), HttpStatus.OK);
    }

    @GetMapping("/{batchId}")
    public ResponseEntity<List<BatchSale>> getBatchSaleByBatchId(@PathVariable Long batchId) {
        return new ResponseEntity<List<BatchSale>>(batchSaleService.getBatchSalesByBatchId(batchId), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody BatchSaleUpdateDTO dto) {
        BatchSale sale = batchSaleService.findById(id);

        // Mapping desde DTO
        sale.setQuantitySold(dto.getQuantitySold());
        sale.setKgTotal(dto.getKgTotal());
        sale.setSaleTotal(dto.getSaleTotal());
        sale.setKgGut(dto.getKgGut());
        sale.setDate(dto.getDate());

        // Relaciones
        if (dto.getBatchId() != null) {
            Batch batch = batchService.findById(dto.getBatchId());
            sale.setBatch(batch);
        }

        if (dto.getEmployeeId() != null) {
            Employee employee = employeeService.findById(dto.getEmployeeId())
                    .orElseThrow();
            sale.setEmployee(employee);
        }

        // Aquí sí puedes usar save()
        batchSaleService.save(sale);

        return ResponseEntity.ok(sale);
    }

    @PostMapping
    public ResponseEntity<BatchSaleCreateResponseDTO> create(
            @RequestBody BatchSaleCreateRequestDTO dto) {
        BatchSaleCreateResponseDTO response = batchSaleService.createBatchSale(dto);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/search")
    public BatchSaleSearchResponseDTO search(
            @Valid @RequestBody BatchSaleSearchRequestDTO request) {
        return batchSaleService.search(request);
    }

}
