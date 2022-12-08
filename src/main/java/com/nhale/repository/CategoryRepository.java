package com.nhale.repository;

import com.nhale.pojos.Category;

import java.util.List;

public interface CategoryRepository {
    List<Category> getCategories();

    Category geCategoryById(int id);
}
