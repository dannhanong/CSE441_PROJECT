package com.ktpm1.restaurant.services;

import com.ktpm1.restaurant.models.Category;

import java.util.List;

public interface CategoryService {
    public Category createCategory(Category category);
    public Category updateCategory(Category category, Long id);
    public void deleteCategory(Long id);
    public Category getCategory(Long id);
    public List<Category> getAllCategories();
}
