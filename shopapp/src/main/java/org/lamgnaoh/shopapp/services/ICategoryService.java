package org.lamgnaoh.shopapp.services;

import org.lamgnaoh.shopapp.dtos.CategoryDTO;
import org.lamgnaoh.shopapp.models.Category;
import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO category);
    Category getCategoryById(long id);
    List<Category> getAllCategories();
    Category updateCategory(long categoryId, CategoryDTO category);
    void deleteCategory(long id);
}
