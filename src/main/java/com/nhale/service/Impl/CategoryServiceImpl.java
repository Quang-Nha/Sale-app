/*
 *
 */
package com.nhale.service.Impl;

import com.nhale.pojos.Category;
import com.nhale.repository.CategoryRepository;
import com.nhale.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LeNha
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getCategories() {
        return this.categoryRepository.getCategories();
    }

    @Override
    public Category geCategoryById(int id) {
        return this.categoryRepository.geCategoryById(id);
    }
}
