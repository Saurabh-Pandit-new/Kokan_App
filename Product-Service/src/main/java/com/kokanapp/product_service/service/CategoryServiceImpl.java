package com.kokanapp.product_service.service;


import com.kokanapp.product_service.entity.Category;
import com.kokanapp.product_service.repository.CategoryRepository;
import com.kokanapp.product_service.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category createCategory(Category category) {
        // Optional: prevent duplicates
        Optional<Category> existing = categoryRepository.findByName(category.getName());
        return existing.orElseGet(() -> categoryRepository.save(category));
    }

    @Override
    public Category findOrCreateCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseGet(() -> categoryRepository.save(new Category(null, name)));
    }
}

