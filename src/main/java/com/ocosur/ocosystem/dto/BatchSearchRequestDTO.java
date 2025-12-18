package com.ocosur.ocosystem.dto;

import java.time.LocalDate;
import java.util.List;

public record BatchSearchRequestDTO(
        List<Long> branchIds,
        LocalDate startDate,
        LocalDate endDate) {
}
