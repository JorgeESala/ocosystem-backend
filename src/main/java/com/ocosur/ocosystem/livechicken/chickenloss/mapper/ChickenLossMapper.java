package com.ocosur.ocosystem.livechicken.chickenloss.mapper;

import com.ocosur.ocosystem.livechicken.chickenloss.ChickenLoss;
import com.ocosur.ocosystem.livechicken.chickenloss.dto.ChickenLossCreateRequestDTO;
import com.ocosur.ocosystem.livechicken.chickenloss.dto.ChickenLossResponseDTO;
import com.ocosur.ocosystem.livechicken.chickenloss.dto.ChickenLossUpdateRequestDTO;
import com.ocosur.ocosystem.livechicken.inboundbatch.InboundBatch;

public class ChickenLossMapper {

    private ChickenLossMapper() {
    }

    public static ChickenLoss toEntity(
            ChickenLossCreateRequestDTO dto,
            InboundBatch batch) {

        if (dto == null) {
            return null;
        }

        return ChickenLoss.builder()
                .quantity(dto.getQuantity())
                .weight(dto.getWeight())
                .lossAmount(dto.getLossAmount())
                .batch(batch)
                .date(dto.getDate())
                .build();
    }
    public static ChickenLoss toEntity(
            ChickenLossUpdateRequestDTO dto,
            InboundBatch batch) {

        if (dto == null) {
            return null;
        }

        return ChickenLoss.builder()
                .quantity(dto.getQuantity())
                .weight(dto.getWeight())
                .lossAmount(dto.getLossAmount())
                .batch(batch)
                .date(dto.getDate())
                .build();
    }

    public static ChickenLossResponseDTO toResponse(ChickenLoss entity) {

        if (entity == null) {
            return null;
        }

        ChickenLossResponseDTO dto = new ChickenLossResponseDTO();
        dto.setId(entity.getId());
        dto.setQuantity(entity.getQuantity());
        dto.setWeight(entity.getWeight());
        dto.setLossAmount(entity.getLossAmount());
        dto.setDate(entity.getDate());
        if (entity.getBatch() != null) {
            dto.setBatchId(entity.getBatch().getId());
        }

        return dto;
    }
}
