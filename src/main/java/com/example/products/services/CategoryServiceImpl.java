package com.example.products.services;

import com.example.products.dto.CategoryDTO;
import com.example.products.entities.Category;
import com.example.products.repositories.CategoryRepository;
import com.example.products.services.exceptions.DataIntegrityException;
import com.example.products.services.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository repository;

    @Override
    public CategoryDTO create(CategoryDTO dto) {
        Category category = this.repository.save(new Category(null, dto.getName()));
        return new CategoryDTO(category);
    }

    @Override
    public void update(Long id, CategoryDTO dto) {
        Category category = this.repository.findById(id).orElseThrow(() -> new NotFoundException("Entity not found."));
        category.setName(dto.getName());
        this.repository.save(category);
    }

    @Override
    public void delete(Long id){
        try {
            this.repository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possivel excluir uma categoria que possui produtos");
        }
    }

    @Override
    public CategoryDTO findById(Long id) {
        Category category = this.repository.findById(id).orElseThrow(() -> new NotFoundException("Entity not found."));
        return new CategoryDTO(category);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
        Page<Category> list = repository.findAll(pageRequest);
        return list.map(CategoryDTO::new);
    }

}
