package com.ocosur.ocosystem.livechicken.chickenloss;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.livechicken.chickenloss.dto.ChickenLossCreateRequestDTO;
import com.ocosur.ocosystem.livechicken.chickenloss.dto.ChickenLossResponseDTO;
import com.ocosur.ocosystem.livechicken.chickenloss.dto.ChickenLossUpdateRequestDTO;
import com.ocosur.ocosystem.livechicken.chickenloss.mapper.ChickenLossMapper;
import com.ocosur.ocosystem.livechicken.inboundbatch.InboundBatch;
import com.ocosur.ocosystem.livechicken.inboundbatch.InboundBatchRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ChickenLossService {
    private final ChickenLossRepository chickenLossRepository;
    private final InboundBatchRepository batchRepository;

    public List<ChickenLossResponseDTO> getAllChickenLosses() {

        List<ChickenLoss> chickenLosses = chickenLossRepository.findAll();
        return chickenLosses.stream().map(ChickenLossMapper::toResponse).toList();
    }

    public ChickenLossResponseDTO create(
            ChickenLossCreateRequestDTO dto) {
        InboundBatch batch = batchRepository.findById(dto.getBatchId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Batch not found: " + dto.getBatchId()));

        ChickenLoss chickenLoss = ChickenLossMapper.toEntity(dto, batch);
        ChickenLoss saved = chickenLossRepository.save(chickenLoss);
        return ChickenLossMapper.toResponse(saved);
    }
    public ChickenLossResponseDTO update(
            Long batchId,
            ChickenLossUpdateRequestDTO dto) {
        InboundBatch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Batch not found: " + batchId));

        ChickenLoss chickenLoss = ChickenLossMapper.toEntity(dto, batch);
        ChickenLoss saved = chickenLossRepository.save(chickenLoss);
        return ChickenLossMapper.toResponse(saved);
    }
}
