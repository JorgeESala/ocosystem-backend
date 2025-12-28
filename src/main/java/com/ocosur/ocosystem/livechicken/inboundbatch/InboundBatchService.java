package com.ocosur.ocosystem.livechicken.inboundbatch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.livechicken.inboundbatch.dto.InboundBatchCreateRequestDTO;
import com.ocosur.ocosystem.livechicken.inboundbatch.dto.InboundBatchResponseDTO;
import com.ocosur.ocosystem.livechicken.inboundbatch.dto.InboundBatchUpdateRequestDTO;
import com.ocosur.ocosystem.livechicken.supplier.Supplier;
import com.ocosur.ocosystem.livechicken.supplier.SupplierRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class InboundBatchService {

    private final InboundBatchRepository inboundBatchRepository;
    private final SupplierRepository supplierRepository;

    /*
     * ======================
     * GET ALL
     * ======================
     */
    @Transactional
    public Page<InboundBatchResponseDTO> getAll(Pageable pageable) {
        return inboundBatchRepository
                .findAllByOrderByDateDesc(pageable)
                .map(this::toResponseDTO);
    }
    @Transactional
    public Page<InboundBatchResponseDTO> getLatest(int limit) {
        Pageable pageable = PageRequest.of(0,limit);
        return inboundBatchRepository
                .findAllByOrderByDateDesc(pageable)
                .map(this::toResponseDTO);
    }

    /*
     * ======================
     * GET BY ID
     * ======================
     */
    @Transactional
    public InboundBatchResponseDTO getById(Long id) {
        InboundBatch batch = inboundBatchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "InboundBatch not found with id " + id));

        return toResponseDTO(batch);
    }

    /*
     * ======================
     * CREATE
     * ======================
     */
    public InboundBatchResponseDTO create(InboundBatchCreateRequestDTO dto) {

        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Supplier not found with id " + dto.getSupplierId()));

        InboundBatch batch = new InboundBatch();
        batch.setSupplier(supplier);
        batch.setDate(dto.getDate());
        batch.setRealWeight(dto.getRealWeight());
        batch.setDeclaredWeight(dto.getDeclaredWeight());
        batch.setChickenQuantity(dto.getChickenQuantity());
        batch.setPricePerKg(dto.getPricePerKg());

        return toResponseDTO(
                inboundBatchRepository.save(batch));
    }

    /*
     * ======================
     * UPDATE
     * ======================
     */
    public InboundBatchResponseDTO update(Long id, InboundBatchUpdateRequestDTO dto) {

        InboundBatch batch = inboundBatchRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "InboundBatch not found with id " + id));

        Supplier supplier = supplierRepository.findById(dto.getSupplierId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Supplier not found with id " + dto.getSupplierId()));

        batch.setSupplier(supplier);
        batch.setDate(dto.getDate());
        batch.setRealWeight(dto.getRealWeight());
        batch.setDeclaredWeight(dto.getDeclaredWeight());
        batch.setChickenQuantity(dto.getChickenQuantity());
        batch.setPricePerKg(dto.getPricePerKg());

        return toResponseDTO(
                inboundBatchRepository.save(batch));
    }

    /*
     * ======================
     * DELETE
     * ======================
     */
    public void delete(Long id) {
        if (!inboundBatchRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "InboundBatch not found with id " + id);
        }
        inboundBatchRepository.deleteById(id);
    }

    /*
     * ======================
     * MAPPER
     * ======================
     */
    private InboundBatchResponseDTO toResponseDTO(InboundBatch batch) {
        return InboundBatchResponseDTO.builder()
                .id(batch.getId())
                .supplierId(batch.getSupplier().getId())
                .supplierName(batch.getSupplier().getName())
                .date(batch.getDate())
                .realWeight(batch.getRealWeight())
                .declaredWeight(batch.getDeclaredWeight())
                .chickenQuantity(batch.getChickenQuantity())
                .pricePerKg(batch.getPricePerKg())
                .totalPaid(batch.getTotalPaid())
                .avgWeight(batch.getAvgWeight())
                .build();
    }
}
