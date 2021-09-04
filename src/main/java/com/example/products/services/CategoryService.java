package com.example.products.services;

import com.example.products.dto.CategoryDTO;
import com.example.products.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDTO create(CategoryDTO dto);
    void update(Long id, CategoryDTO dto);
    void delete(Long id);
    CategoryDTO findById(Long id);
    Page<CategoryDTO> findAllPaged(Pageable pageable);
    Category find(Long id);
}
