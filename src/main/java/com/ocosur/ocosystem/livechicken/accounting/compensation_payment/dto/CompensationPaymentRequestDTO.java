package com.ocosur.ocosystem.livechicken.accounting.compensation_payment.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CompensationPaymentRequestDTO {

    public Long branchId;
    public Long supplierId;
    public Long cedisId;

    public BigDecimal amount;
    public LocalDate date;
    public String folio;
    public String note;
}

