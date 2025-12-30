package com.ocosur.ocosystem.livechicken.expense;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.livechicken.expense.dto.ExpenseCreateRequestDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.ExpenseResponseDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.ExpenseUpdateRequestDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/live-chicken/expenses")
public class LiveChickenExpenseController {
    @Autowired
    private LiveChickenExpenseService expenseService;

    @GetMapping
    public List<ExpenseResponseDTO> getExpenses() {
        return expenseService.getExpenses();
    }
    @GetMapping("/latest")
    public List<ExpenseResponseDTO> getLatestExpenses() {
        return expenseService.getExpenses();
    }
    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> create(
            @RequestBody @Valid ExpenseCreateRequestDTO dto) {
        ExpenseResponseDTO created = expenseService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ExpenseResponseDTO update(
            @PathVariable Long id,
            @RequestBody ExpenseUpdateRequestDTO expense) {
        return expenseService.update(id, expense);
    }
}