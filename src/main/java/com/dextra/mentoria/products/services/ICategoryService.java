package com.dextra.mentoria.products.services;

import com.dextra.mentoria.products.dtos.request.CategoryRequest;
import com.dextra.mentoria.products.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface ICategoryService {
    Category create(CategoryRequest request);
    void update(Long id, CategoryRequest request);
    void delete(Long id);
    Category findById(Long id);
    Page<Category> findAllPaged(Pageable pageable);
    List<Category> findAllById(Set<Long> ids);
}
