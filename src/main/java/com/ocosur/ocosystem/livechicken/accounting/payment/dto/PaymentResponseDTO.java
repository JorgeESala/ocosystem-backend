package com.ocosur.ocosystem.livechicken.accounting.payment.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.ocosur.ocosystem.livechicken.accounting.payment.Payment;
import com.ocosur.ocosystem.livechicken.accounting.payment.PaymentMethod;

@Getter
@Builder
public class PaymentResponseDTO {

    private Long id;
    private BigDecimal amount;
    private LocalDate paymentDate;
    private PaymentMethod paymentMethod;
    private String folio;
    private String notes;

    public static PaymentResponseDTO from(Payment payment) {
        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .amount(payment.getAmount())
                .paymentDate(payment.getPaymentDate())
                .paymentMethod(payment.getPaymentMethod())
                .folio(payment.getFolio())
                .notes(payment.getNote())
                .build();
    }
}
