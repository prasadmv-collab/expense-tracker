package com.prasad.expensetracker.service;

import com.prasad.expensetracker.dto.expense.ExpenseRequest;
import com.prasad.expensetracker.dto.expense.ExpenseResponse;

import java.util.List;

public interface ExpenseService {

    ExpenseResponse createExpense(ExpenseRequest request);

    List<ExpenseResponse> getAllExpenses();

    ExpenseResponse getExpenseById(Long id);

    ExpenseResponse updateExpense(Long id, ExpenseRequest request);

    void deleteExpense(Long id);
}