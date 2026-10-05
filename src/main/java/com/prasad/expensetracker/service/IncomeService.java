package com.prasad.expensetracker.service;

import com.prasad.expensetracker.dto.income.IncomeRequest;
import com.prasad.expensetracker.dto.income.IncomeResponse;

import java.util.List;

public interface IncomeService {

    IncomeResponse createIncome(IncomeRequest request);

    List<IncomeResponse> getAllIncomes();

    IncomeResponse getIncomeById(Long id);

    IncomeResponse updateIncome(Long id, IncomeRequest request);

    void deleteIncome(Long id);
}