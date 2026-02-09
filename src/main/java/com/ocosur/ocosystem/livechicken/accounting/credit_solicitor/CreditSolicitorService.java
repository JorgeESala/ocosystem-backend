package com.ocosur.ocosystem.livechicken.accounting.credit_solicitor;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.livechicken.accounting.credit_solicitor.dto.CreditSolicitorResponseDTO;
import com.ocosur.ocosystem.livechicken.accounting.credit_solicitor.mapper.CreditSolicitorMapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreditSolicitorService {

    private final CreditSolicitorRepository creditSolicitorRepository;
    private final CreditSolicitorMapper mapper;

    public List<CreditSolicitorResponseDTO> getAll() {
        return creditSolicitorRepository.findAll().stream().map(mapper::toResponseDTO).toList();
    }

    public CreditSolicitor getById(Long solicitorId) {
        return creditSolicitorRepository.findById(solicitorId)
                .orElseThrow(() -> new EntityNotFoundException("Solicitante con id " + solicitorId + " no encontrado"));
    }
}
