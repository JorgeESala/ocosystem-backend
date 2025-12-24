package com.ocosur.ocosystem.livechicken.expense;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.livechicken.expense.dto.ExpenseResponse;

@RestController
@RequestMapping("/api/live-chicken/expenses")
public class LiveChickenExpenseController {
    @Autowired
    private LiveChickenExpenseService expenseService;

    @GetMapping
    public List<ExpenseResponse> getExpenses() {
        return expenseService.getExpenses();
    }
    @GetMapping("/latest")
    public List<ExpenseResponse> getLatestExpenses() {
        return expenseService.getExpenses();
    }
}
