package com.dextra.mentoria.products.services;

import com.dextra.mentoria.products.dto.CategoryDTO;
import com.dextra.mentoria.products.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CategoryService {
    CategoryDTO create(CategoryDTO dto);
    CategoryDTO update(Long id, CategoryDTO dto);
    void delete(Long id);
    CategoryDTO findById(Long id);
    Page<CategoryDTO> findAllPaged(Pageable pageable);
    Category find(Long id);
}
