package com.ocosur.ocosystem.livechicken.accounting.payment_application;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.livechicken.accounting.accounts_payable.AccountsPayable;
import com.ocosur.ocosystem.livechicken.accounting.accounts_payable_movement.AccountsPayableMovement;
import com.ocosur.ocosystem.livechicken.accounting.accounts_payable_movement.AccountsPayableMovementRepository;
import com.ocosur.ocosystem.livechicken.accounting.payment.Payment;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentApplicationService {

    private final PaymentApplicationRepository paymentApplicationRepository;
    private final AccountsPayableMovementRepository movementRepository;

   

    /**
     * Apply part (or all) of a payment to a specific accounts payable.
     */
    @Transactional
    public void applyPayment(
            Payment payment,
            AccountsPayable accountsPayable,
            BigDecimal amount,
            String notes) {

        validateAmount(amount);

        // Validate remaining payment amount
        BigDecimal alreadyApplied = paymentApplicationRepository
                .sumAppliedAmountByPayment(payment.getId());

        BigDecimal remainingPaymentAmount = payment.getAmount().subtract(alreadyApplied);

        if (remainingPaymentAmount.compareTo(amount) < 0) {
            throw new IllegalStateException(
                    "Cannot apply more than remaining payment amount");
        }

        //  Validate accounts payable balance
        if (accountsPayable.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException(
                    "Cannot apply more than accounts payable balance");
        }

        // Create payment application
        PaymentApplication application = PaymentApplication.create(payment, accountsPayable, amount);

        paymentApplicationRepository.save(application);

        // Create movement
        AccountsPayableMovement movement = AccountsPayableMovement.payment(
                accountsPayable,
                amount,
                payment.getPaymentDate(),
                notes);

        movementRepository.save(movement);

        // Update accounts payable balance
        accountsPayable.applyPayment(amount);
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than zero");
        }
    }
}
