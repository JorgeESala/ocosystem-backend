package com.ocosur.ocosystem.livechicken.expense;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ocosur.ocosystem.livechicken.expense.dto.ExpenseCreateRequestDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.ExpenseDetailResponseDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.ExpenseResponseDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.ExpenseUpdateRequestDTO;
import com.ocosur.ocosystem.security.RequireBusiness;

import lombok.RequiredArgsConstructor;
@RequireBusiness("LIVE_CHICKEN")
@RestController("liveChickenExpenseController")
@RequestMapping("/api/live-chicken/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @PostMapping
    public ResponseEntity<ExpenseResponseDTO> create(
            @RequestBody ExpenseCreateRequestDTO request) {
        ExpenseResponseDTO response = expenseService.createExpense(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/latest")
    public List<ExpenseResponseDTO> getLatest() {
        return expenseService.getLatest();
    }

    @GetMapping("/{id}")
    public ExpenseDetailResponseDTO getById(@PathVariable Long id) {
        return expenseService.getById(id);
    }

    @GetMapping
    public List<ExpenseResponseDTO> getBetween(
            @RequestParam LocalDate start,
            @RequestParam LocalDate end) {
        return expenseService.getBetween(start, end);
    }

    @PutMapping("/{id}")
    public ExpenseResponseDTO update(
            @PathVariable Long id,
            @RequestBody ExpenseUpdateRequestDTO request) {
        return expenseService.updateExpense(id, request);
    }

}