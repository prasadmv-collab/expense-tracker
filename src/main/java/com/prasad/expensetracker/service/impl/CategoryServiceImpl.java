package com.prasad.expensetracker.service.impl;

import com.prasad.expensetracker.dto.category.CategoryRequest;
import com.prasad.expensetracker.dto.category.CategoryResponse;
import com.prasad.expensetracker.entity.Category;
import com.prasad.expensetracker.entity.User;
import com.prasad.expensetracker.exception.ResourceNotFoundException;
import com.prasad.expensetracker.repository.CategoryRepository;
import com.prasad.expensetracker.repository.UserRepository;
import com.prasad.expensetracker.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = Category.builder()
                .name(request.getName())
                .type(request.getType())
                .user(user)
                .build();

        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponse(
                savedCategory.getId(),
                savedCategory.getName(),
                savedCategory.getType()
        );
    }

    @Override
    public List<CategoryResponse> getAllCategories() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return categoryRepository.findByUser(user)
                .stream()
                .map(category -> new CategoryResponse(
                        category.getId(),
                        category.getName(),
                        category.getType()
                ))
                .toList();
    }

    @Override
    public CategoryResponse getCategoryById(Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getType()
        );
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        category.setName(request.getName());
        category.setType(request.getType());

        Category updatedCategory = categoryRepository.save(category);

        return new CategoryResponse(
                updatedCategory.getId(),
                updatedCategory.getName(),
                updatedCategory.getType()
        );
    }

    @Override
    public void deleteCategory(Long id) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = categoryRepository
                .findByIdAndUser(id, user)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found"));

        categoryRepository.delete(category);
    }
}