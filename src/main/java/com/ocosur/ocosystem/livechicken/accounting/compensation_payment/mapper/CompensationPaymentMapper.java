package com.ocosur.ocosystem.livechicken.accounting.compensation_payment.mapper;

import org.springframework.stereotype.Component;

import com.ocosur.ocosystem.livechicken.accounting.compensation_payment.CompensationPayment;
import com.ocosur.ocosystem.livechicken.accounting.compensation_payment.dto.CompensationPaymentResponseDTO;

@Component
public class CompensationPaymentMapper {
    public CompensationPaymentResponseDTO toResponseDTO(CompensationPayment entity) {
        return new CompensationPaymentResponseDTO(
                entity.getId(),
                entity.getAmount(),
                entity.getFolio()
        );
    }
}
