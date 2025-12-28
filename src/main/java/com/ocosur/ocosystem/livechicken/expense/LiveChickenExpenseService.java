package com.ocosur.ocosystem.livechicken.expense;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ocosur.ocosystem.livechicken.expense.dto.ExpenseCreateRequestDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.ExpenseResponseDTO;
import com.ocosur.ocosystem.livechicken.expense.dto.ExpenseUpdateRequestDTO;
import com.ocosur.ocosystem.livechicken.expense.mapper.ExpenseMapper;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LiveChickenExpenseService {

    private final LiveChickenExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    public List<ExpenseResponseDTO> getExpenses() {
        return expenseRepository.findAll().stream().map(c -> new ExpenseResponseDTO(c.getId(), c.getReason(), c.getAmount(), c.getDate())).toList();
    }

    public List<ExpenseResponseDTO> getLatestExpenses() {
        Pageable pageable = PageRequest.of(0, 15);
        Page<LiveChickenExpense> latestExpenses = expenseRepository.findAllByOrderByIdDesc(pageable);
        return latestExpenses.getContent().stream().map(c -> new ExpenseResponseDTO(c.getId(), c.getReason(), c.getAmount(), c.getDate())).toList();
    }

    public ExpenseResponseDTO create(
            ExpenseCreateRequestDTO dto
    ) {
       

        LiveChickenExpense expense = LiveChickenExpense.builder()
                .reason(dto.getReason())
                .amount(dto.getAmount())
                .date(dto.getDate())
                .build();

        LiveChickenExpense saved = expenseRepository.save(expense);

        return expenseMapper.toResponseDTO(saved);
    }
    public ExpenseResponseDTO update(
            Long expenseId,
            ExpenseUpdateRequestDTO dto
    ) {
        LiveChickenExpense expense = expenseRepository
                .findById(expenseId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Sale not found: " + expenseId));

        

        expense.setAmount(dto.getAmount());
        expense.setReason(dto.getReason());
        expense.setDate(dto.getDate());

        LiveChickenExpense updated = expenseRepository.save(expense);

        return expenseMapper.toResponseDTO(updated);
    }

}
