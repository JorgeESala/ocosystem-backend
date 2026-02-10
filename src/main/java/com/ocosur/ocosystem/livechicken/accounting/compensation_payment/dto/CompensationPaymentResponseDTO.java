package com.ocosur.ocosystem.livechicken.accounting.compensation_payment.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

 @AllArgsConstructor
 @NoArgsConstructor
public class CompensationPaymentResponseDTO {
    public Long id;
    public BigDecimal amount;
    public String folio;
}
