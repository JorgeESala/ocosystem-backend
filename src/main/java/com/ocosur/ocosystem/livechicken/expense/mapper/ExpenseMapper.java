package com.ocosur.ocosystem.livechicken.expense.mapper;

import org.springframework.stereotype.Component;

import com.ocosur.ocosystem.livechicken.expense.LiveChickenExpense;
import com.ocosur.ocosystem.livechicken.expense.dto.ExpenseResponseDTO;

@Component
public class ExpenseMapper {
    private ExpenseMapper() {
        // util class
    }

    public ExpenseResponseDTO toResponseDTO(LiveChickenExpense entity) {
        if (entity == null)
            return null;

        ExpenseResponseDTO dto = new ExpenseResponseDTO();

        dto.setId(entity.getId());
        dto.setReason(entity.getReason());
        dto.setAmount(entity.getAmount());
        dto.setDate(entity.getDate());
        return dto;
    }
}
