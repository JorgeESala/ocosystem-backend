package com.ocosur.ocosystem.livechicken.batchsale.mapper;

import com.ocosur.ocosystem.livechicken.batchsale.InboundBatchSale;
import com.ocosur.ocosystem.livechicken.batchsale.dto.InboundBatchSaleResponseDTO;

public class InboundBatchSaleMapper {

    private InboundBatchSaleMapper() {
        // util class
    }

    public static InboundBatchSaleResponseDTO toResponseDTO(InboundBatchSale entity) {
        if (entity == null)
            return null;

        InboundBatchSaleResponseDTO dto = new InboundBatchSaleResponseDTO();

        dto.setId(entity.getId());
        dto.setBatchId(entity.getBatch().getId());
        dto.setQuantitySold(entity.getQuantitySold());
        dto.setKgSold(entity.getKgSold());
        dto.setKgSent(entity.getKgSent());
        dto.setSaleTotal(entity.getSaleTotal());
        dto.setDate(entity.getDate());

        if (entity.getEmployee() != null) {
            dto.setEmployeeId(entity.getEmployee().getId());
            dto.setEmployeeName(entity.getEmployee().getName());
        }

        return dto;
    }
}
