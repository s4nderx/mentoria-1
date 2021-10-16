package com.dextra.mentoria.products.controllers;

import com.dextra.mentoria.products.dto.request.ProductRequest;
import com.dextra.mentoria.products.dto.response.ProductResponse;
import com.dextra.mentoria.products.services.IProductService;
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

    private final IProductService service;

    public ProductController(IProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> insert(@Valid @RequestBody ProductRequest request){
        ProductResponse response = this.service.create(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.getId()).toUri();
        return  ResponseEntity.created(uri).body(response);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(NO_CONTENT)
    public void update(@Valid @RequestBody ProductRequest request, @PathVariable Long id){
        this.service.update(id, request);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(NO_CONTENT)
    public void delete(@PathVariable Long id) {
        this.service.delete(id);
    }

    @GetMapping()
    @ResponseStatus(OK)
    public Page<ProductResponse> findAll(Pageable pageable){
        return this.service.findAllPaged(pageable);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(OK)
    public ProductResponse findById(@PathVariable Long id){
        return this.service.findById(id);
    }

}
