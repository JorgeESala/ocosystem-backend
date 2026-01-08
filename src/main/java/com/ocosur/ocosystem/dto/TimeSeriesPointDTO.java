package com.ocosur.ocosystem.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeSeriesPointDTO {

    private LocalDate date; 
    private String series;
    private BigDecimal value;
}
