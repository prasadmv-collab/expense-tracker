package com.prasad.expensetracker.service;

import com.prasad.expensetracker.dto.reports.MonthlyExpenseResponse;
import com.prasad.expensetracker.dto.reports.MonthlyFinancialResponse;

import java.util.List;

public interface ReportsService {

    List<MonthlyExpenseResponse> getMonthlyExpenseHistory();

    List<MonthlyFinancialResponse> getMonthlyFinancialHistory();
}