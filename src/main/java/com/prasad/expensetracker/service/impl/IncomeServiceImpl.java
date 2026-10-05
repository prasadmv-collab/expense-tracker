package com.prasad.expensetracker.service.impl;

import com.prasad.expensetracker.dto.income.IncomeRequest;
import com.prasad.expensetracker.dto.income.IncomeResponse;
import com.prasad.expensetracker.entity.Income;
import com.prasad.expensetracker.entity.User;
import com.prasad.expensetracker.exception.ResourceNotFoundException;
import com.prasad.expensetracker.repository.IncomeRepository;
import com.prasad.expensetracker.repository.UserRepository;
import com.prasad.expensetracker.service.IncomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;

    @Override
    public IncomeResponse createIncome(IncomeRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Income income = Income.builder()
                .title(request.getTitle())
                .amount(request.getAmount())
                .description(request.getDescription())
                .date(request.getDate())
                .user(user)
                .build();

        Income savedIncome = incomeRepository.save(income);

        return IncomeResponse.builder()
                .id(savedIncome.getId())
                .title(savedIncome.getTitle())
                .amount(savedIncome.getAmount())
                .description(savedIncome.getDescription())
                .date(savedIncome.getDate())
                .build();
    }

    @Override
    public List<IncomeResponse> getAllIncomes() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return incomeRepository.findByUser(user)
                .stream()
                .map(income -> IncomeResponse.builder()
                        .id(income.getId())
                        .title(income.getTitle())
                        .amount(income.getAmount())
                        .description(income.getDescription())
                        .date(income.getDate())
                        .build())
                .toList();
    }

    @Override
    public IncomeResponse getIncomeById(Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Income income = incomeRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Income not found"));

        return IncomeResponse.builder()
                .id(income.getId())
                .title(income.getTitle())
                .amount(income.getAmount())
                .description(income.getDescription())
                .date(income.getDate())
                .build();
    }

    @Override
    public IncomeResponse updateIncome(
            Long id,
            IncomeRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Income income = incomeRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Income not found"));

        income.setTitle(request.getTitle());
        income.setAmount(request.getAmount());
        income.setDescription(request.getDescription());
        income.setDate(request.getDate());

        Income updatedIncome =
                incomeRepository.save(income);

        return IncomeResponse.builder()
                .id(updatedIncome.getId())
                .title(updatedIncome.getTitle())
                .amount(updatedIncome.getAmount())
                .description(updatedIncome.getDescription())
                .date(updatedIncome.getDate())
                .build();
    }

    @Override
    public void deleteIncome(Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Income income = incomeRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Income not found"));

        incomeRepository.delete(income);
    }
}