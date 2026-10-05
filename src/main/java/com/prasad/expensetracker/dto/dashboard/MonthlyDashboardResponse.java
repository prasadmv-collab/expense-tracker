package com.prasad.expensetracker.dto.dashboard;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyDashboardResponse {

    private String month;

    private BigDecimal totalIncome;

    private BigDecimal totalExpense;

    private BigDecimal balance;
}