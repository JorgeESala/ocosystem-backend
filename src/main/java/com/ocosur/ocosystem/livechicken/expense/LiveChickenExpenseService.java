package com.ocosur.ocosystem.livechicken.expense;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.livechicken.expense.dto.ExpenseResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LiveChickenExpenseService {

    private final LiveChickenExpenseRepository expenseRepository;

    public List<ExpenseResponse> getExpenses() {
        return expenseRepository.findAll().stream().map(c -> new ExpenseResponse(c.getId(), c.getReason(), c.getAmount().toString())).toList();
    }

    public List<ExpenseResponse> getLatestExpenses() {
        Pageable pageable = PageRequest.of(0, 15);
        Page<LiveChickenExpense> latestExpenses = expenseRepository.findAllByOrderByIdDesc(pageable);
        return latestExpenses.getContent().stream().map(c -> new ExpenseResponse(c.getId(), c.getReason(), c.getAmount().toString())).toList();
    }


}
