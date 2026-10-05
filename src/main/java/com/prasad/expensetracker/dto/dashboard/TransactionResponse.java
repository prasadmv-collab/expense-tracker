package com.prasad.expensetracker.dto.dashboard;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {

    private Long id;

    private String type;

    private String title;

    private BigDecimal amount;

    private String description;

    private LocalDate date;

    private String categoryName;
}