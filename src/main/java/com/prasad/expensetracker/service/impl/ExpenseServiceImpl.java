package com.prasad.expensetracker.service.impl;

import com.prasad.expensetracker.dto.expense.ExpenseRequest;
import com.prasad.expensetracker.dto.expense.ExpenseResponse;
import com.prasad.expensetracker.entity.Category;
import com.prasad.expensetracker.entity.Expense;
import com.prasad.expensetracker.entity.User;
import com.prasad.expensetracker.exception.ResourceNotFoundException;
import com.prasad.expensetracker.repository.CategoryRepository;
import com.prasad.expensetracker.repository.ExpenseRepository;
import com.prasad.expensetracker.repository.UserRepository;
import com.prasad.expensetracker.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public ExpenseResponse createExpense(ExpenseRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Category category = categoryRepository
                .findByIdAndUser(request.getCategoryId(), user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        Expense expense = Expense.builder()
                .title(request.getTitle())
                .amount(request.getAmount())
                .description(request.getDescription())
                .date(request.getDate())
                .category(category)
                .user(user)
                .build();

        Expense savedExpense = expenseRepository.save(expense);

        return ExpenseResponse.builder()
                .id(savedExpense.getId())
                .title(savedExpense.getTitle())
                .amount(savedExpense.getAmount())
                .description(savedExpense.getDescription())
                .date(savedExpense.getDate())
                .categoryName(savedExpense.getCategory().getName())
                .build();
    }

    @Override
    public List<ExpenseResponse> getAllExpenses() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return expenseRepository.findByUser(user)
                .stream()
                .map(expense -> ExpenseResponse.builder()
                        .id(expense.getId())
                        .title(expense.getTitle())
                        .amount(expense.getAmount())
                        .description(expense.getDescription())
                        .date(expense.getDate())
                        .categoryName(expense.getCategory().getName())
                        .build())
                .toList();
    }

    @Override
    public ExpenseResponse getExpenseById(Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Expense expense = expenseRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Expense not found"));

        return ExpenseResponse.builder()
                .id(expense.getId())
                .title(expense.getTitle())
                .amount(expense.getAmount())
                .description(expense.getDescription())
                .date(expense.getDate())
                .categoryName(expense.getCategory().getName())
                .build();
    }

    @Override
    public ExpenseResponse updateExpense(
            Long id,
            ExpenseRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Expense expense = expenseRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Expense not found"));

        Category category = categoryRepository
                .findByIdAndUser(request.getCategoryId(), user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        expense.setTitle(request.getTitle());
        expense.setAmount(request.getAmount());
        expense.setDescription(request.getDescription());
        expense.setDate(request.getDate());
        expense.setCategory(category);

        Expense updatedExpense =
                expenseRepository.save(expense);

        return ExpenseResponse.builder()
                .id(updatedExpense.getId())
                .title(updatedExpense.getTitle())
                .amount(updatedExpense.getAmount())
                .description(updatedExpense.getDescription())
                .date(updatedExpense.getDate())
                .categoryName(
                        updatedExpense.getCategory().getName()
                )
                .build();
    }

    @Override
    public void deleteExpense(Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Expense expense = expenseRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Expense not found"));

        expenseRepository.delete(expense);
    }
}