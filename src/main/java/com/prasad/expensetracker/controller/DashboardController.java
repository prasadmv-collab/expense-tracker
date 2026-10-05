package com.prasad.expensetracker.controller;

import com.prasad.expensetracker.dto.dashboard.CategoryExpenseResponse;
import com.prasad.expensetracker.dto.dashboard.DashboardResponse;
import com.prasad.expensetracker.dto.dashboard.MonthlyDashboardResponse;
import com.prasad.expensetracker.dto.dashboard.TransactionResponse;
import com.prasad.expensetracker.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardResponse getDashboard() {
        return dashboardService.getDashboard();
    }

    @GetMapping("/monthly")
    public MonthlyDashboardResponse getMonthlyDashboard(
            @RequestParam int year,
            @RequestParam int month) {

        return dashboardService.getMonthlyDashboard(year, month);
    }

    @GetMapping("/categories")
    public List<CategoryExpenseResponse> getCategoryExpenses() {
        return dashboardService.getCategoryExpenses();
    }

    @GetMapping("/transactions")
    public List<TransactionResponse> getRecentTransactions() {
        return dashboardService.getRecentTransactions();
    }
}