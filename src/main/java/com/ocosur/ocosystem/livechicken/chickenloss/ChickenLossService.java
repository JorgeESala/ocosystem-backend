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
import jakarta.transaction.Transactional;
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

    @Transactional
    public List<ChickenLossResponseDTO> getAllByInboundBatchId(Long inboundBatchId) {
        return chickenLossRepository
                .findAllByBatch_IdOrderByDateDesc(inboundBatchId)
                .stream()
                .map(ChickenLossMapper::toResponse)
                .toList();
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
            Long lossId,
            ChickenLossUpdateRequestDTO dto) {
        ChickenLoss loss = chickenLossRepository
                .findById(lossId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Loss not found: " + lossId));

        if (dto.getBatchId() != null) {
            InboundBatch batch = batchRepository
                    .findById(dto.getBatchId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Batch not found: " + dto.getBatchId()));
            loss.setBatch(batch);
        }

        if (dto.getQuantity() != null) {
            loss.setQuantity(dto.getQuantity());
        }

        if (dto.getWeight() != null) {
            loss.setWeight(dto.getWeight());
        }

        if (dto.getLossAmount() != null) {
            loss.setLossAmount(dto.getLossAmount());
        }

        if (dto.getDate() != null) {
            loss.setDate(dto.getDate());
        }

        ChickenLoss updated = chickenLossRepository.save(loss);

        return ChickenLossMapper.toResponse(updated);
    }

}
