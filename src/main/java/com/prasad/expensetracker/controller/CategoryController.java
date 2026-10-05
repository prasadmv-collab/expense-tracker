package com.prasad.expensetracker.controller;

import com.prasad.expensetracker.dto.category.CategoryRequest;
import com.prasad.expensetracker.dto.category.CategoryResponse;
import com.prasad.expensetracker.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // Create Category
    @PostMapping
    public CategoryResponse createCategory(
            @Valid @RequestBody CategoryRequest request) {

        return categoryService.createCategory(request);
    }

    // Get All Categories
    @GetMapping
    public List<CategoryResponse> getAllCategories() {

        return categoryService.getAllCategories();
    }

    // Get Category By ID
    @GetMapping("/{id}")
    public CategoryResponse getCategoryById(@PathVariable Long id) {

        return categoryService.getCategoryById(id);
    }

    // Update Category
    @PutMapping("/{id}")
    public CategoryResponse updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest request) {

        return categoryService.updateCategory(id, request);
    }

    // Delete Category
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long id) {

        categoryService.deleteCategory(id);
    }
}