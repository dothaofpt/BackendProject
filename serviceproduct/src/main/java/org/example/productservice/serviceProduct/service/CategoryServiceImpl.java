package org.example.productservice.serviceProduct.service;

import org.example.productservice.serviceProduct.dto.CategoryDTO;
import org.example.productservice.serviceProduct.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public int createCategory(CategoryDTO categoryDTO) {
        // Kiểm tra hợp lệ trước khi tạo Category
        if (categoryDTO.getName() == null || categoryDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        return categoryRepository.createCategory(categoryDTO);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.getAllCategories();
    }

    @Override
    public CategoryDTO getCategoryById(Integer id) {
        CategoryDTO category = categoryRepository.getCategoryById(id);
        if (category == null) {
            throw new IllegalArgumentException("Category not found with id: " + id);
        }
        return category;
    }

    @Override
    public int updateCategory(Integer id, CategoryDTO categoryDTO) {
        // Kiểm tra hợp lệ trước khi cập nhật Category
        if (categoryDTO.getName() == null || categoryDTO.getName().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        return categoryRepository.updateCategory(id, categoryDTO);
    }

    @Override
    public int deleteCategory(Integer id) {
        return categoryRepository.deleteCategory(id);
    }
}
