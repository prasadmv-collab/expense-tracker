package com.prasad.expensetracker.dto.expense;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseResponse {

    private Long id;

    private String title;

    private BigDecimal amount;

    private String description;

    private LocalDate date;

    private String categoryName;
}