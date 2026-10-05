package com.prasad.expensetracker.controller;

import com.prasad.expensetracker.dto.income.IncomeRequest;
import com.prasad.expensetracker.dto.income.IncomeResponse;
import com.prasad.expensetracker.service.IncomeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/incomes")
@RequiredArgsConstructor
public class IncomeController {

    private final IncomeService incomeService;

    @PostMapping
    public IncomeResponse createIncome(
            @Valid @RequestBody IncomeRequest request) {

        return incomeService.createIncome(request);
    }

    @GetMapping
    public List<IncomeResponse> getAllIncomes() {

        return incomeService.getAllIncomes();
    }

    @GetMapping("/{id}")
    public IncomeResponse getIncomeById(
            @PathVariable Long id) {

        return incomeService.getIncomeById(id);
    }

    @PutMapping("/{id}")
    public IncomeResponse updateIncome(
            @PathVariable Long id,
            @Valid @RequestBody IncomeRequest request) {

        return incomeService.updateIncome(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIncome(
            @PathVariable Long id) {

        incomeService.deleteIncome(id);
    }
}