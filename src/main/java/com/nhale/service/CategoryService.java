package com.nhale.service;

import com.nhale.pojos.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getCategories();

    Category geCategoryById(int id);
}
