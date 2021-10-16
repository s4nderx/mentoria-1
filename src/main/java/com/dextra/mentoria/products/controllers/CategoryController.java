package com.dextra.mentoria.products.controllers;

import com.dextra.mentoria.products.dto.request.CategoryRequest;
import com.dextra.mentoria.products.dto.response.CategoryResponse;
import com.dextra.mentoria.products.services.ICategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    private final ICategoryService service;

    public CategoryController(ICategoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> insert(@Valid @RequestBody CategoryRequest request){
        CategoryResponse categoryResponse = this.service.create(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(categoryResponse.getId()).toUri();
        return  ResponseEntity.created(uri).body(categoryResponse);
    }

    @GetMapping
    @ResponseStatus(OK)
    public Page<CategoryResponse> findAll(Pageable pageable){
        return this.service.findAllPaged(pageable);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(OK)
    public CategoryResponse findById(@PathVariable Long id){
        return this.service.findById(id);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        this.service.delete(id);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(NO_CONTENT)
    public void update(@Valid @RequestBody CategoryRequest dto, @PathVariable Long id) {
        this.service.update(id, dto);
    }

}
