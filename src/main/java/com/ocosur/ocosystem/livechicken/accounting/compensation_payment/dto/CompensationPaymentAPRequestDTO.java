package com.ocosur.ocosystem.livechicken.accounting.compensation_payment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Getter;
@Getter
public class CompensationPaymentAPRequestDTO {

    private Long branchCedisAccountsPayableId;
    private Long cedisSupplierAccountsPayableId;
    
    private BigDecimal amount;
    private LocalDate date;
    private String folio;
    private String note;
}
