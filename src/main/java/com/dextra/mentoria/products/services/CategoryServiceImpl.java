package com.dextra.mentoria.products.services;

import com.dextra.mentoria.products.dto.CategoryDTO;
import com.dextra.mentoria.products.entities.Category;
import com.dextra.mentoria.products.repositories.CategoryRepository;
import com.dextra.mentoria.products.services.exceptions.DataIntegrityException;
import com.dextra.mentoria.products.services.exceptions.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
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
    public CategoryDTO update(Long id, CategoryDTO dto) {
        Category category = this.find(id);
        category.setName(dto.getName());
        category = this.repository.save(category);
        return new CategoryDTO(category);
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
    public CategoryDTO findById(Long id) {
        return new CategoryDTO(this.find(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(Pageable pageable) {
        return repository.findAll(pageable).map(CategoryDTO::new);
    }

    @Override
    public Category find(Long id) {
        return this.repository.findById(id).orElseThrow(() -> new NotFoundException("Entity not found."));
    }

}
