package com.enterprise.pos.service;

import com.enterprise.pos.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);
    List<CategoryDto> getCategoriesByStore(Long storeId);
   CategoryDto getCategoryById(Long id);
    CategoryDto updateCategory(Long id , CategoryDto categoryDto);
    void deleteCategory(Long id);
}
