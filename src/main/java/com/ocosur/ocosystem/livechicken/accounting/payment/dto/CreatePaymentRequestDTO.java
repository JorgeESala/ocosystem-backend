package com.ocosur.ocosystem.livechicken.accounting.payment.dto;

import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ocosur.ocosystem.livechicken.accounting.payment.PaymentMethod;

@Getter
public class CreatePaymentRequestDTO {

    // payer (branch)
    private Long payerId;

    // receiver (branch / cedis)
    private Long receiverId;

    private BigDecimal amount;
    private LocalDate paymentDate;
    private PaymentMethod paymentMethod;
    private String folio;
    private String notes;
}
