package com.prasad.expensetracker.service.impl;

import com.prasad.expensetracker.dto.reports.MonthlyExpenseResponse;
import com.prasad.expensetracker.dto.reports.MonthlyFinancialResponse;
import com.prasad.expensetracker.entity.User;
import com.prasad.expensetracker.repository.ExpenseRepository;
import com.prasad.expensetracker.repository.IncomeRepository;
import com.prasad.expensetracker.repository.UserRepository;
import com.prasad.expensetracker.service.ReportsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportsServiceImpl implements ReportsService {

    private final ExpenseRepository expenseRepository;
    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    @Override
    public List<MonthlyExpenseResponse> getMonthlyExpenseHistory() {

        User user = getCurrentUser();

        return expenseRepository.getMonthlyExpenseHistory(user)
                .stream()
                .map(result -> MonthlyExpenseResponse.builder()
                        .month((String) result[0])
                        .totalExpense((BigDecimal) result[1])
                        .build())
                .toList();
    }

    @Override
    public List<MonthlyFinancialResponse> getMonthlyFinancialHistory() {

        User user = getCurrentUser();

        List<Object[]> incomeResults =
                incomeRepository.getMonthlyIncomeHistory(user);

        List<Object[]> expenseResults =
                expenseRepository.getMonthlyExpenseHistory(user);

        Map<String, BigDecimal> incomeMap = new HashMap<>();
        Map<String, BigDecimal> expenseMap = new HashMap<>();

        for (Object[] result : incomeResults) {
            incomeMap.put(
                    (String) result[0],
                    (BigDecimal) result[1]
            );
        }

        for (Object[] result : expenseResults) {
            expenseMap.put(
                    (String) result[0],
                    (BigDecimal) result[1]
            );
        }

        List<String> months = new ArrayList<>();

        months.addAll(incomeMap.keySet());

        for (String month : expenseMap.keySet()) {
            if (!months.contains(month)) {
                months.add(month);
            }
        }

        months.sort(String::compareTo);

        return months.stream()
                .map(month -> MonthlyFinancialResponse.builder()
                        .month(month)
                        .income(
                                incomeMap.getOrDefault(
                                        month,
                                        BigDecimal.ZERO
                                )
                        )
                        .expense(
                                expenseMap.getOrDefault(
                                        month,
                                        BigDecimal.ZERO
                                )
                        )
                        .build())
                .toList();
    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }
}