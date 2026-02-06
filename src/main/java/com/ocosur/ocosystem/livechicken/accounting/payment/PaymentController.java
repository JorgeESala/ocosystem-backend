package com.ocosur.ocosystem.livechicken.accounting.payment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ocosur.ocosystem.livechicken.accounting.accounting_entity.AccountingEntity;
import com.ocosur.ocosystem.livechicken.accounting.accounting_entity.AccountingEntityService;
import com.ocosur.ocosystem.livechicken.accounting.payment.dto.CreatePaymentRequestDTO;
import com.ocosur.ocosystem.livechicken.accounting.payment.dto.PaymentResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounting/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final AccountingEntityService accountingEntityService;


    // ----------------------------------------------------
    // Create & apply payment (FIFO)
    // ----------------------------------------------------

    @PostMapping("/fifo")
    public ResponseEntity<PaymentResponseDTO> createAndApplyFifo(
            @RequestBody CreatePaymentRequestDTO request
    ) {

        AccountingEntity payer =
                accountingEntityService.getById(
                        request.getPayerId()
                );

        AccountingEntity receiver =
                accountingEntityService.getById(
                        request.getReceiverId()
                );

        Payment payment = paymentService.createAndApplyPaymentFifo(
                payer,
                receiver,
                request.getAmount(),
                request.getPaymentDate(),
                request.getPaymentMethod(),
                request.getFolio(),
                request.getNotes()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(PaymentResponseDTO.from(payment));
    }

    // ----------------------------------------------------
    // Create payment only (no application)
    // ----------------------------------------------------

    @PostMapping
    public ResponseEntity<PaymentResponseDTO> createPayment(
            @RequestBody CreatePaymentRequestDTO request
    ) {

        AccountingEntity payer =
                accountingEntityService.getById(
                        request.getPayerId()
                );

        AccountingEntity receiver =
                accountingEntityService.getById(
                        request.getReceiverId()
                );

        Payment payment = paymentService.createPayment(
                payer,
                receiver,
                request.getAmount(),
                request.getPaymentDate(),
                request.getPaymentMethod(),
                request.getFolio(),
                request.getNotes()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(PaymentResponseDTO.from(payment));
    }
}
