package com.ocosur.ocosystem.livechicken.accounting.compensation_payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.livechicken.accounting.accounting_entity.AccountingEntity;
import com.ocosur.ocosystem.livechicken.accounting.accounts_payable.AccountsPayable;
import com.ocosur.ocosystem.livechicken.accounting.accounts_payable.AccountsPayableRepository;
import com.ocosur.ocosystem.livechicken.accounting.accounts_payable_movement.AccountsPayableMovement;
import com.ocosur.ocosystem.livechicken.accounting.accounts_payable_movement.AccountsPayableMovementRepository;
import com.ocosur.ocosystem.livechicken.accounting.compensation_payment.dto.CompensationPaymentAPRequestDTO;
import com.ocosur.ocosystem.livechicken.accounting.compensation_payment.dto.CompensationPaymentResponseDTO;
import com.ocosur.ocosystem.livechicken.accounting.compensation_payment.mapper.CompensationPaymentMapper;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompensationPaymentService {

    private final AccountsPayableRepository accountsPayableRepository;
    private final CompensationPaymentRepository compensationPaymentRepository;
    private final AccountsPayableMovementRepository movementRepository;
    private final CompensationPaymentMapper compensationPaymentMapper;

    @Transactional
    public void registerCompensationPaymentFIFO(CompensationPayment payment) {

        BigDecimal remaining = payment.getAmount();

        List<AccountsPayable> branchDebts = accountsPayableRepository
                .findByDebtorAndCreditorAndBalanceGreaterThanOrderByCreatedAtAsc(
                        payment.getBranch(),
                        payment.getCedis(),
                        BigDecimal.ZERO);

        List<AccountsPayable> supplierDebts = accountsPayableRepository
                .findByDebtorAndCreditorAndBalanceGreaterThanOrderByCreatedAtAsc(
                        payment.getCedis(),
                        payment.getSupplier(),
                        BigDecimal.ZERO);

        validateSufficientBalance(branchDebts, supplierDebts, remaining);

        compensationPaymentRepository.save(payment);

        applyFifo(branchDebts, remaining, payment, "Compensación sucursal " + payment.getBranch().getDisplayName()
                + " a CEDIS " + payment.getCedis().getDisplayName());
        applyFifo(supplierDebts, remaining, payment, "Compensación CEDIS " + payment.getCedis().getDisplayName() + " a "
                + payment.getSupplier().getDisplayName());
    }

    @Transactional
    public void registerCompensationPayment(
            CompensationPayment payment,
            AccountsPayable debtBranchCedis,
            AccountsPayable debtCedisSupplier) {

        BigDecimal amount = payment.getAmount();

        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("El pago debe ser positivo");
        }

        if (debtBranchCedis.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException(
                    "El pago supera la cuenta con el CEDIS " + debtBranchCedis.getCreditor().getDisplayName());
        }

        if (debtCedisSupplier.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException(
                    "El pago supera la cuenta con el proveedor " + debtCedisSupplier.getCreditor().getDisplayName());
        }

        compensationPaymentRepository.save(payment);

        AccountsPayableMovement m1 = AccountsPayableMovement.compensation(
                debtBranchCedis,
                amount,
                payment,
                "Pago recibido vía sucursal");

        AccountsPayableMovement m2 = AccountsPayableMovement.compensation(
                debtCedisSupplier,
                amount,
                payment,
                "Pago recibido vía sucursal");

        movementRepository.save(m1);
        movementRepository.save(m2);

        debtBranchCedis.apply(m1);
        debtCedisSupplier.apply(m2);

    }

    @Transactional
    public CompensationPaymentResponseDTO registerCompensationPaymentWithAP(
            CompensationPaymentAPRequestDTO request) {

        BigDecimal amount = request.getAmount();
        AccountsPayable debtBranchCedis = accountsPayableRepository
                .findById(request.getBranchCedisAccountsPayableId()).orElseThrow(() -> new IllegalArgumentException(
                        "AccountsPayable not found with id: " + request.getBranchCedisAccountsPayableId()));

        AccountsPayable debtCedisSupplier = accountsPayableRepository
                .findById(request.getCedisSupplierAccountsPayableId()).orElseThrow(() -> new IllegalArgumentException(
                        "AccountsPayable not found with id: " + request.getCedisSupplierAccountsPayableId()));

        AccountingEntity branch = debtBranchCedis.getDebtor();
        AccountingEntity cedis = debtBranchCedis.getCreditor();
        AccountingEntity supplier = debtCedisSupplier.getCreditor();

        if (amount.signum() <= 0) {
            throw new IllegalArgumentException("El pago debe ser positivo");
        }

        if (debtBranchCedis.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException(
                    "El pago supera la cuenta con el CEDIS " + debtBranchCedis.getCreditor().getDisplayName());
        }

        if (debtCedisSupplier.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException(
                    "El pago supera la cuenta con el proveedor " + debtCedisSupplier.getCreditor().getDisplayName());
        }

        CompensationPayment payment = new CompensationPayment();
        payment.setBranch(branch);
        payment.setSupplier(supplier);
        payment.setCedis(cedis);
        payment.setAmount(amount);
        payment.setDate(
                request.getDate() != null ? request.getDate() : LocalDate.now());
        payment.setFolio(request.getFolio());
        payment.setNote(request.getNote());

        CompensationPayment registeredPayment = compensationPaymentRepository.save(payment);

        AccountsPayableMovement m1 = AccountsPayableMovement.compensation(
                debtBranchCedis,
                amount,
                payment,
                "Pago recibido vía sucursal");

        AccountsPayableMovement m2 = AccountsPayableMovement.compensation(
                debtCedisSupplier,
                amount,
                payment,
                "Pago recibido vía sucursal");

        movementRepository.save(m1);
        movementRepository.save(m2);

        debtBranchCedis.apply(m1);
        debtCedisSupplier.apply(m2);
            return compensationPaymentMapper.toResponseDTO(registeredPayment);
    }

    /* ========= FIFO core ========= */

    private void applyFifo(
            List<AccountsPayable> debts,
            BigDecimal totalAmount,
            CompensationPayment payment,
            String note) {
        BigDecimal remaining = totalAmount;

        for (AccountsPayable debt : debts) {
            if (remaining.signum() <= 0)
                break;

            BigDecimal toApply = debt.getBalance().min(remaining);

            AccountsPayableMovement m = AccountsPayableMovement.compensation(
                    debt,
                    toApply,
                    payment,
                    note);

            movementRepository.save(m);
            debt.apply(m);

            remaining = remaining.subtract(toApply);
        }
    }

    /* ========= Validaciones ========= */

    private void validateSufficientBalance(
            List<AccountsPayable> branchDebts,
            List<AccountsPayable> supplierDebts,
            BigDecimal amount) {

        BigDecimal branchTotal = branchDebts.stream()
                .map(AccountsPayable::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal supplierTotal = supplierDebts.stream()
                .map(AccountsPayable::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (branchTotal.compareTo(amount) < 0) {
            throw new IllegalStateException("Sucursal no tiene deuda suficiente");
        }

        if (supplierTotal.compareTo(amount) < 0) {
            throw new IllegalStateException("CEDIS no tiene deuda suficiente con el proveedor");
        }
    }
}
