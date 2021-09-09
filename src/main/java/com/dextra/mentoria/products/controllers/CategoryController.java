package com.dextra.mentoria.products.controllers;

import com.dextra.mentoria.products.services.CategoryService;
import com.dextra.mentoria.products.dto.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CategoryDTO> insert(@Valid @RequestBody CategoryDTO dto){
        dto = this.service.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return  ResponseEntity.created(uri).body(dto);
    }

    @GetMapping
    @ResponseStatus(OK)
    public Page<CategoryDTO> findAll(Pageable pageable){
        return this.service.findAllPaged(pageable);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(OK)
    public CategoryDTO findById(@PathVariable Long id){
        return this.service.findById(id);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        this.service.delete(id);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(OK)
    public CategoryDTO update(@Valid @RequestBody CategoryDTO dto, @PathVariable Long id) {
        return this.service.update(id, dto);
    }

}
