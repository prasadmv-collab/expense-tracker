package com.prasad.expensetracker.dto.category;

import com.prasad.expensetracker.enums.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoryResponse {

    private Long id;

    private String name;

    private CategoryType type;
}