package com.prasad.expensetracker.service.impl;

import com.prasad.expensetracker.dto.dashboard.CategoryExpenseResponse;
import com.prasad.expensetracker.dto.dashboard.DashboardResponse;
import com.prasad.expensetracker.dto.dashboard.MonthlyDashboardResponse;
import com.prasad.expensetracker.dto.dashboard.TransactionResponse;
import com.prasad.expensetracker.entity.Expense;
import com.prasad.expensetracker.entity.Income;
import com.prasad.expensetracker.entity.User;
import com.prasad.expensetracker.repository.ExpenseRepository;
import com.prasad.expensetracker.repository.IncomeRepository;
import com.prasad.expensetracker.repository.UserRepository;
import com.prasad.expensetracker.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    @Override
    public DashboardResponse getDashboard() {

        User user = getCurrentUser();

        BigDecimal totalIncome =
                incomeRepository.getTotalIncomeByUser(user);

        BigDecimal totalExpense =
                expenseRepository.getTotalExpenseByUser(user);

        BigDecimal balance =
                totalIncome.subtract(totalExpense);

        return DashboardResponse.builder()
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(balance)
                .build();
    }

    @Override
    public MonthlyDashboardResponse getMonthlyDashboard(
            int year,
            int month) {

        User user = getCurrentUser();

        YearMonth yearMonth =
                YearMonth.of(year, month);

        LocalDate startDate =
                yearMonth.atDay(1);

        LocalDate endDate =
                yearMonth.atEndOfMonth();

        BigDecimal totalIncome =
                incomeRepository.getMonthlyIncome(
                        user,
                        startDate,
                        endDate
                );

        BigDecimal totalExpense =
                expenseRepository.getMonthlyExpense(
                        user,
                        startDate,
                        endDate
                );

        BigDecimal balance =
                totalIncome.subtract(totalExpense);

        return MonthlyDashboardResponse.builder()
                .month(yearMonth.toString())
                .totalIncome(totalIncome)
                .totalExpense(totalExpense)
                .balance(balance)
                .build();
    }

    @Override
    public List<CategoryExpenseResponse> getCategoryExpenses() {

        User user = getCurrentUser();

        return expenseRepository.getExpenseByCategory(user)
                .stream()
                .map(result -> CategoryExpenseResponse.builder()
                        .categoryName((String) result[0])
                        .totalAmount((BigDecimal) result[1])
                        .build())
                .toList();
    }

    @Override
    public List<TransactionResponse> getRecentTransactions() {

        User user = getCurrentUser();

        List<TransactionResponse> transactions =
                new ArrayList<>();

        // Get all expenses
        List<Expense> expenses =
                expenseRepository.findByUser(user);

        for (Expense expense : expenses) {

            transactions.add(
                    TransactionResponse.builder()
                            .id(expense.getId())
                            .type("EXPENSE")
                            .title(expense.getTitle())
                            .amount(expense.getAmount())
                            .description(expense.getDescription())
                            .date(expense.getDate())
                            .categoryName(
                                    expense.getCategory() != null
                                            ? expense.getCategory().getName()
                                            : null
                            )
                            .build()
            );
        }

        // Get all incomes
        List<Income> incomes =
                incomeRepository.findByUser(user);

        for (Income income : incomes) {

            transactions.add(
                    TransactionResponse.builder()
                            .id(income.getId())
                            .type("INCOME")
                            .title(income.getTitle())
                            .amount(income.getAmount())
                            .description(income.getDescription())
                            .date(income.getDate())
                            .categoryName(null)
                            .build()
            );
        }

        // Sort newest transactions first
        transactions.sort(
                Comparator.comparing(
                        TransactionResponse::getDate
                ).reversed()
        );

        // Return maximum 10 transactions
        return transactions.stream()
                .limit(10)
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