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

    private static final Class<CategoryResponse> responseClass = CategoryResponse.class;

    private final CategoryRepository repository;

    private final ModelMapper modelMapper;

    public CategoryService(CategoryRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryResponse create(CategoryRequest request) {
        Category category = this.repository.save(new Category(null, request.getName()));
        return modelMapper.map(category, responseClass);
    }

    @Override
    public void update(Long id, CategoryRequest request) {
        Category category = this.find(id);
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
    public CategoryResponse findById(Long id) {
        Category category = this.find(id);
        return modelMapper.map(category, responseClass);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryResponse> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable).map(category -> modelMapper.map(category, responseClass));
    }

    @Override
    public Category find(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new NotFoundException("Entity not found."));
    }

    @Override
    public List<Category> findAllById(Set<Long> ids) {
        return this.repository.findAllById(ids);
    }

}
