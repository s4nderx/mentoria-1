package com.dextra.mentoria.products.controllers;

import com.dextra.mentoria.products.dtos.request.CategoryRequest;
import com.dextra.mentoria.products.entities.Category;
import com.dextra.mentoria.products.services.ICategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController implements ICategoryController {

    private final ICategoryService service;

    public CategoryController(ICategoryService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Category> insert(CategoryRequest request) {
        Category category = this.service.create(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(category.getId()).toUri();
        return ResponseEntity.created(uri).body(category);
    }

    @Override
    public Page<Category> findAll(Pageable pageable) {
        return this.service.findAllPaged(pageable);
    }

    @Override
    public Category findById(Long id) {
        return this.service.findById(id);
    }

    @Override
    public void delete(Long id) {
        this.service.delete(id);
    }

    @Override
    public void update(CategoryRequest dto, Long id) {
        this.service.update(id, dto);
    }

}
