package com.ocosur.ocosystem.livechicken.accounting.credit_solicitor.mapper;

import org.springframework.stereotype.Component;

import com.ocosur.ocosystem.livechicken.accounting.credit_solicitor.CreditSolicitor;
import com.ocosur.ocosystem.livechicken.accounting.credit_solicitor.dto.CreditSolicitorResponseDTO;

@Component
public class CreditSolicitorMapper {
    public CreditSolicitorResponseDTO toResponseDTO(CreditSolicitor entity) {
        return new CreditSolicitorResponseDTO(
                entity.getId(),
                entity.getName()
        );
    }
}
