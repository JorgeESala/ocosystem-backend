package com.ocosur.ocosystem.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;
@Data
public class SearchByBranchDTO {
    private List<Integer> branchIds;
    private LocalDate start;
    private LocalDate end;
}
