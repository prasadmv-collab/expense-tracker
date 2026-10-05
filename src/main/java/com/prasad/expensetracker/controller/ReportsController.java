package com.prasad.expensetracker.controller;

import com.prasad.expensetracker.dto.reports.MonthlyExpenseResponse;
import com.prasad.expensetracker.dto.reports.MonthlyFinancialResponse;
import com.prasad.expensetracker.service.ReportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportsController {

    private final ReportsService reportsService;

    @GetMapping("/monthly-expenses")
    public List<MonthlyExpenseResponse> getMonthlyExpenseHistory() {
        return reportsService.getMonthlyExpenseHistory();
    }

    @GetMapping("/monthly-financial")
    public List<MonthlyFinancialResponse> getMonthlyFinancialHistory() {
        return reportsService.getMonthlyFinancialHistory();
    }
}