package com.dextra.mentoria.products.controllers;

import com.dextra.mentoria.products.dto.ProductDTO;
import com.dextra.mentoria.products.services.ProductService;
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
@RequestMapping(value = "/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto){
        dto = this.service.create(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return  ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(OK)
    public ProductDTO update(@Valid @RequestBody ProductDTO dto, @PathVariable Long id){
        return this.service.update(id, dto);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        this.service.delete(id);
    }

    @GetMapping()
    @ResponseStatus(OK)
    public Page<ProductDTO> findAll(Pageable pageable){
        return this.service.findAllPaged(pageable);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(OK)
    public ProductDTO findById(@PathVariable Long id){
        return this.service.findById(id);
    }

}
