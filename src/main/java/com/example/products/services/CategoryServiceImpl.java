package com.example.products.services;

import com.example.products.dto.CategoryDTO;
import com.example.products.entities.Category;
import com.example.products.repositories.CategoryRepository;
import com.example.products.services.exceptions.DataIntegrityException;
import com.example.products.services.exceptions.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public CategoryDTO create(CategoryDTO dto) {
        Category category = this.repository.save(new Category(null, dto.getName()));
        return new CategoryDTO(category);
    }

    @Override
    public void update(Long id, CategoryDTO dto) {
        Category category = this.find(id);
        category.setName(dto.getName());
        this.repository.save(category);
    }

    @Override
    public void delete(Long id){
        try {
            this.repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("It is not possible to delete a category that has products");
        }
    }

    @Override
    public CategoryDTO findById(Long id) {
        Category category = this.find(id);
        return new CategoryDTO(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(Pageable pageable) {
        Page<Category> list = repository.findAll(pageable);
        return list.map(CategoryDTO::new);
    }

    @Override
    public Category find(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new NotFoundException("Entity not found."));
    }

}
