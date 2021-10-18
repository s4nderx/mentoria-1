package com.dextra.mentoria.products.services;

import com.dextra.mentoria.products.dtos.request.CategoryRequest;
import com.dextra.mentoria.products.dtos.response.CategoryResponse;
import com.dextra.mentoria.products.entities.Category;
import com.dextra.mentoria.products.repositories.CategoryRepository;
import com.dextra.mentoria.products.services.exceptions.DataIntegrityException;
import com.dextra.mentoria.products.services.exceptions.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class CategoryService implements ICategoryService {

    private final CategoryRepository repository;

    public CategoryService(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category create(CategoryRequest request) {
        return this.repository.save(new Category(null, request.getName()));
    }

    @Override
    public void update(Long id, CategoryRequest request) {
        Category category = this.findById(id);
        category.setName(request.getName());
        this.repository.save(category);
    }

    @Override
    public void delete(Long id){
        try {
            this.repository.deleteById(id);
        } catch (EmptyResultDataAccessException e){
            throw new NotFoundException("Id not found " + id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("It is not possible to delete a category that has products");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Category> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Category findById(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new NotFoundException("Entity not found."));
    }

    @Override
    public List<Category> findAllById(Set<Long> ids) {
        return this.repository.findAllById(ids);
    }

}
