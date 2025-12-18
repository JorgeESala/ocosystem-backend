package com.ocosur.ocosystem.mapper;

import org.springframework.stereotype.Component;

import com.ocosur.ocosystem.dto.BatchCreateRequestDTO;
import com.ocosur.ocosystem.dto.BatchCreateResponseDTO;
import com.ocosur.ocosystem.dto.BatchItemResponseDTO;
import com.ocosur.ocosystem.model.Batch;

@Component
public class BatchMapper {

    public Batch toEntity(BatchCreateRequestDTO dto) {
        Batch batch = new Batch();
        batch.setKgTotal(dto.kgTotal());
        batch.setPricePerKg(dto.pricePerKg());
        batch.setDate(dto.date());
        batch.setProvider(dto.provider());
        batch.setChickenQuantity(dto.chickenQuantity());
        return batch;
    }

    public BatchCreateResponseDTO toCreateResponse(Batch batch) {
        return new BatchCreateResponseDTO(
                batch.getId(),
                batch.getBranch().getId(),
                batch.getKgTotal(),
                batch.getPricePerKg(),
                batch.getPriceTotal(),
                batch.getAvgChickenWeight(),
                batch.getDate(),
                batch.getProvider(),
                batch.getChickenQuantity());
    }

    public BatchItemResponseDTO toItemResponse(Batch batch) {
        return new BatchItemResponseDTO(
                batch.getId(),
                batch.getBranch().getId(),
                batch.getBranch().getName(),
                batch.getKgTotal(),
                batch.getPricePerKg(),
                batch.getPriceTotal(),
                batch.getAvgChickenWeight(),
                batch.getDate(),
                batch.getProvider(),
                batch.getChickenQuantity());
    }
}
