package com.dextra.mentoria.products.services;

import com.dextra.mentoria.products.dto.request.CategoryRequest;
import com.dextra.mentoria.products.dto.response.CategoryResponse;
import com.dextra.mentoria.products.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface ICategoryService {
    CategoryResponse create(CategoryRequest request);
    void update(Long id, CategoryRequest request);
    void delete(Long id);
    CategoryResponse findById(Long id);
    Page<CategoryResponse> findAllPaged(Pageable pageable);
    Category find(Long id);
    List<Category> findAllById(Set<Long> ids);
}
