package com.dextra.mentoria.products.controllers;

import com.dextra.mentoria.products.dtos.request.CategoryRequest;
import com.dextra.mentoria.products.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

public interface ICategoryController {
    @PostMapping
    ResponseEntity<Category> insert(@Valid @RequestBody CategoryRequest request);

    @GetMapping
    @ResponseStatus(OK)
    Page<Category> findAll(Pageable pageable);

    @GetMapping(value = "/{id}")
    @ResponseStatus(OK)
    Category findById(@PathVariable Long id);

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(NO_CONTENT)
    void delete(@PathVariable Long id);

    @PutMapping(value = "/{id}")
    @ResponseStatus(NO_CONTENT)
    void update(@Valid @RequestBody CategoryRequest dto, @PathVariable Long id);
}
