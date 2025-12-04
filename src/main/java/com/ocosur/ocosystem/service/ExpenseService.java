package com.ocosur.ocosystem.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.dto.ExpenseDTO;
import com.ocosur.ocosystem.model.Branch;
import com.ocosur.ocosystem.model.Expense;
import com.ocosur.ocosystem.model.ExpenseCategory;
import com.ocosur.ocosystem.repository.BranchRepository;
import com.ocosur.ocosystem.repository.ExpenseCategoryRepository;
import com.ocosur.ocosystem.repository.ExpenseRepository;

@Service
public class ExpenseService {

    @Autowired
    ExpenseRepository expenseRepository;
    @Autowired
    BranchRepository branchRepository;
    @Autowired
    ExpenseCategoryRepository categoryRepository;

    public List<Expense> getExpenses() {
        return expenseRepository.findAll();
    }

    public Expense saveExpense(ExpenseDTO expenseDTO) {
        Branch branch = branchRepository.findById(expenseDTO.branchId())
                .orElseThrow(() -> new RuntimeException("Branch no encontrada"));

        ExpenseCategory category = categoryRepository.findById(expenseDTO.categoryId())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        Expense expense = Expense.builder()
                .reason(expenseDTO.reason())
                .amount(expenseDTO.amount())
                .branch(branch)
                .date(expenseDTO.date())
                .category(category)
                .build();
        return expenseRepository.save(expense);
    }

    public List<Expense> getLatestExpensesByBranches(List<Integer> branchIds) {
        Pageable limit = PageRequest.of(0, 10);

        return expenseRepository.findByBranchIdInOrderByDateDesc(branchIds, limit).getContent();
    }

    public Expense updateExpense(Integer id, ExpenseDTO expenseDTO) {
        Expense existing = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));

        existing.setReason(expenseDTO.reason());
        existing.setAmount(expenseDTO.amount());
        existing.setDate(expenseDTO.date());
        existing.setBranch(branchRepository.findById(expenseDTO.branchId()).orElseThrow());
        existing.setCategory(categoryRepository.findById(expenseDTO.categoryId()).orElseThrow());

        return expenseRepository.save(existing);
    }

    public BigDecimal sumBetweenByBranches(LocalDate start, LocalDate end, List<Integer> branchIds) {
        return expenseRepository.sumBetweenByBranches(start, end, branchIds);
    }

    public List<Expense> getLatestExpenses() {
        Pageable limit = PageRequest.of(0, 15);
        return expenseRepository.findAllByOrderByDateDesc(limit).getContent();
    }

    public List<Expense> findByBranchesAndDateRange(
            List<Integer> branchIds,
            LocalDate start,
            LocalDate end) {
        return expenseRepository.findByBranchIdInAndDateBetweenOrderByDateDesc(
                branchIds,
                start,
                end);
    }

}
