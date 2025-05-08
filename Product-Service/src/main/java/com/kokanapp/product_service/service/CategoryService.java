package com.kokanapp.product_service.service;


import com.kokanapp.product_service.entity.Category;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category createCategory(Category category);
    Category findOrCreateCategoryByName(String name);
}

