package com.prasad.expensetracker.service;

import com.prasad.expensetracker.dto.dashboard.CategoryExpenseResponse;
import com.prasad.expensetracker.dto.dashboard.DashboardResponse;
import com.prasad.expensetracker.dto.dashboard.MonthlyDashboardResponse;
import com.prasad.expensetracker.dto.dashboard.TransactionResponse;

import java.util.List;

public interface DashboardService {

    DashboardResponse getDashboard();

    MonthlyDashboardResponse getMonthlyDashboard(int year, int month);

    List<CategoryExpenseResponse> getCategoryExpenses();

    List<TransactionResponse> getRecentTransactions();
}