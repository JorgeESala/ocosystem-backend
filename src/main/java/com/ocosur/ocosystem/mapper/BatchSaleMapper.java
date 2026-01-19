package com.ocosur.ocosystem.mapper;

import com.ocosur.ocosystem.core.employee.model.Employee;
import com.ocosur.ocosystem.dto.BatchSaleCreateRequestDTO;
import com.ocosur.ocosystem.dto.BatchSaleCreateResponseDTO;
import com.ocosur.ocosystem.dto.BatchSaleItemResponseDTO;
import com.ocosur.ocosystem.model.BatchSale;
import com.ocosur.ocosystem.processed.client.Client;

import org.springframework.stereotype.Component;

@Component
public class BatchSaleMapper {

    public BatchSale toEntity(BatchSaleCreateRequestDTO dto) {
        BatchSale sale = new BatchSale();

        sale.setQuantitySold(dto.getQuantitySold());
        sale.setKgTotal(dto.getKgTotal());
        sale.setSaleTotal(dto.getSaleTotal());
        sale.setKgGut(dto.getKgGut());
        sale.setDate(dto.getDate());

        return sale;
    }

    public BatchSaleCreateResponseDTO toCreateResponse(BatchSale sale) {
        return new BatchSaleCreateResponseDTO(
                sale.getId(),
                sale.getBatch().getId(),
                sale.getEmployee().getId(),
                sale.getQuantitySold(),
                sale.getKgTotal(),
                sale.getSaleTotal(),
                sale.getKgGut(),
                sale.getDate());
    }

    public BatchSaleItemResponseDTO toItemResponse(BatchSale sale) {

        Employee emp = sale.getEmployee();
        Client client = sale.getClient();

        return new BatchSaleItemResponseDTO(
                sale.getId(),
                sale.getBatch().getId(),
                sale.getQuantitySold(),
                sale.getKgTotal(),
                sale.getKgGut(),
                sale.getSaleTotal(),
                emp != null ? emp.getId() : null,
                emp != null ? emp.getName() : null,
                client != null ? client.getId() : null,
                client != null ? client.getName() : null,
                sale.getDate());
    }

}
