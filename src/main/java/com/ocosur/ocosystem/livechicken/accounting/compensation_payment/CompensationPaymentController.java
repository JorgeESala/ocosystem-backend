package com.ocosur.ocosystem.livechicken.accounting.compensation_payment;

import java.net.URI;
import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.livechicken.accounting.accounting_entity.AccountingEntity;
import com.ocosur.ocosystem.livechicken.accounting.accounting_entity.AccountingEntityRepository;
import com.ocosur.ocosystem.livechicken.accounting.compensation_payment.dto.CompensationPaymentRequestDTO;
import com.ocosur.ocosystem.livechicken.accounting.compensation_payment.dto.CompensationPaymentResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/compensation-payments")
@RequiredArgsConstructor
public class CompensationPaymentController {

    private final CompensationPaymentService compensationPaymentService;
    private final AccountingEntityRepository accountingEntityRepository;

    @PostMapping
    public ResponseEntity<CompensationPaymentResponseDTO> create(
            @RequestBody CompensationPaymentRequestDTO request) {
        AccountingEntity branch = accountingEntityRepository.findById(request.branchId)
                .orElseThrow(() -> new IllegalArgumentException("Branch not found"));

        AccountingEntity supplier = accountingEntityRepository.findById(request.supplierId)
                .orElseThrow(() -> new IllegalArgumentException("Supplier not found"));

        AccountingEntity cedis = accountingEntityRepository.findById(request.cedisId)
                .orElseThrow(() -> new IllegalArgumentException("CEDIS not found"));

        CompensationPayment payment = new CompensationPayment();
        payment.setBranch(branch);
        payment.setSupplier(supplier);
        payment.setCedis(cedis);
        payment.setAmount(request.amount);
        payment.setDate(
                request.date != null ? request.date : LocalDate.now());
        payment.setFolio(request.folio);
        payment.setNote(request.note);

        compensationPaymentService.registerCompensationPaymentFIFO(payment);

        URI location = URI.create("/api/compensation-payments/" + payment.getId());

        CompensationPaymentResponseDTO response = new CompensationPaymentResponseDTO();
        response.id = payment.getId();
        response.amount = payment.getAmount();
        response.folio = payment.getFolio();

        return ResponseEntity
                .created(location)
                .body(response);
    }

}
