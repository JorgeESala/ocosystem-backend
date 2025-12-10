package com.ocosur.ocosystem.mapper;

import com.ocosur.ocosystem.dto.BatchSaleCreateDTO;
import com.ocosur.ocosystem.dto.BatchSaleResponseDTO;
import com.ocosur.ocosystem.model.Batch;
import com.ocosur.ocosystem.model.BatchSale;
import com.ocosur.ocosystem.model.Employee;

import org.springframework.stereotype.Component;

@Component
public class BatchSaleMapper {

    public BatchSale toEntity(
            BatchSaleCreateDTO dto,
            Batch batch,
            Employee employee) {
        BatchSale sale = new BatchSale();

        sale.setBatch(batch);
        sale.setEmployee(employee);
        sale.setQuantitySold(dto.getQuantitySold());
        sale.setKgTotal(dto.getKgTotal());
        sale.setSaleTotal(dto.getSaleTotal());
        sale.setKgGut(dto.getKgGut());
        sale.setDate(dto.getDate());

        return sale;
    }

    public BatchSaleResponseDTO toResponse(BatchSale sale) {
        return new BatchSaleResponseDTO(
                sale.getId(),
                sale.getBatch().getId(),
                sale.getEmployee().getId(),
                sale.getQuantitySold(),
                sale.getKgTotal(),
                sale.getSaleTotal(),
                sale.getKgGut(),
                sale.getDate());
    }
}
