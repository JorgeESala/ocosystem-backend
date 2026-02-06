package com.ocosur.ocosystem.livechicken.accounting.payment;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.livechicken.accounting.accounting_entity.AccountingEntity;
import com.ocosur.ocosystem.livechicken.accounting.accounts_payable.AccountsPayableService;
import com.ocosur.ocosystem.livechicken.accounting.payment_application.PaymentApplicationService;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AccountsPayableService accountsPayableService;
    private final PaymentApplicationService paymentApplicationService;

    public PaymentService(
            PaymentRepository paymentRepository,
            AccountsPayableService accountsPayableService,
            PaymentApplicationService paymentApplicationService) {
        this.paymentRepository = paymentRepository;
        this.accountsPayableService = accountsPayableService;
        this.paymentApplicationService = paymentApplicationService;
    }

    // ------------------------------------------------------------------
    // Creation
    // ------------------------------------------------------------------

    /**
     * Create a payment WITHOUT applying it to any debt.
     * Useful when the application is manual or deferred.
     */
    @Transactional
    public Payment createPayment(
            AccountingEntity payer,
            AccountingEntity receiver,
            BigDecimal amount,
            LocalDate paymentDate,
            PaymentMethod method,
            String folio,
            String notes) {
        validateAmount(amount);

        Payment payment = Payment.create(
                payer,
                receiver,
                amount,
                paymentDate,
                method,
                folio,
                null,
                null,
                notes);

        return paymentRepository.save(payment);
    }

    /**
     * Create a payment AND apply it using FIFO
     * between a debtor and a creditor.
     */
    @Transactional
    public Payment createAndApplyPaymentFifo(
            AccountingEntity payer,
            AccountingEntity receiver,
            BigDecimal amount,
            LocalDate paymentDate,
            PaymentMethod method,
            String folio,
            String notes) {
        validateAmount(amount);

        // 1️⃣ Create payment
        Payment payment = Payment.create(
                payer,
                receiver,
                amount,
                paymentDate,
                method,
                folio,
                null,
                null,
                notes);

        paymentRepository.save(payment);

        // 2️⃣ Apply FIFO
        accountsPayableService.applyPaymentFifo(
                payment,
                payer,
                receiver,
                notes);

        return payment;
    }

    /**
     * Create a payment AND apply it to a specific accounts payable.
     * Used for manual or exceptional cases.
     */
    @Transactional
    public Payment createAndApplyToSpecificDebt(
            AccountingEntity payer,
            AccountingEntity receiver,
            BigDecimal amount,
            LocalDate paymentDate,
            PaymentMethod method,
            String folio,
            String notes,
            Long accountsPayableId) {
        validateAmount(amount);

        Payment payment = Payment.create(
                payer,
                receiver,
                amount,
                paymentDate,
                method,
                folio,
                null,
                null,
                notes);

        paymentRepository.save(payment);

        paymentApplicationService.applyPayment(
                payment,
                accountsPayableService
                        .getById(accountsPayableId),
                amount,
                notes);

        return payment;
    }

    // ------------------------------------------------------------------
    // Validation
    // ------------------------------------------------------------------

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException(
                    "Payment amount must be greater than zero");
        }
    }
}
