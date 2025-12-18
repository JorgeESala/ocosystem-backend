package com.ocosur.ocosystem.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record WeeklyChickenReportDTO(
    Long branchId,
    OffsetDateTime  weekStart,
    BigDecimal chikenSales,
    BigDecimal chickenExpenses,
    BigDecimal chickenSold,
    BigDecimal chickenBought,
    BigDecimal profit,
    BigDecimal chickengRemaining
) {}
