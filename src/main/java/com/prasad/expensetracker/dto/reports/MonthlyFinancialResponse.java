package com.prasad.expensetracker.dto.reports;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthlyFinancialResponse {

    private String month;

    private BigDecimal income;

    private BigDecimal expense;
}