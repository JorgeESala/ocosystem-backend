package com.ocosur.ocosystem.livechicken.accounting.payment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ocosur.ocosystem.livechicken.accounting.payment.dto.CreateFifoPaymentRequestDTO;
import com.ocosur.ocosystem.livechicken.accounting.payment.dto.CreatePaymentRequestDTO;
import com.ocosur.ocosystem.livechicken.accounting.payment.dto.PaymentResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/accounting/payments")
@RequiredArgsConstructor
public class PaymentController {

        private final PaymentService paymentService;

        // ----------------------------------------------------
        // Create & apply payment (FIFO)
        // ----------------------------------------------------

        @PostMapping("/fifo")
        public ResponseEntity<PaymentResponseDTO> createAndApplyFifo(
                        @RequestBody CreateFifoPaymentRequestDTO request) {

                Payment payment = paymentService.createAndApplyPaymentFifo(request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(PaymentResponseDTO.from(payment));
        }

        // ----------------------------------------------------
        // Create payment only (no application)
        // ----------------------------------------------------

        @PostMapping
        public ResponseEntity<PaymentResponseDTO> createPayment(
                        @RequestBody CreatePaymentRequestDTO request) {

                Payment payment = paymentService.createAndApplyToSpecificDebt(request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(PaymentResponseDTO.from(payment));
        }
}
