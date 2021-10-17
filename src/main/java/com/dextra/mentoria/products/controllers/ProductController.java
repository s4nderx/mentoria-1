package com.dextra.mentoria.products.controllers;

import com.dextra.mentoria.products.dto.request.ProductRequest;
import com.dextra.mentoria.products.dto.response.ProductResponse;
import com.dextra.mentoria.products.entities.Product;
import com.dextra.mentoria.products.services.IProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/products")
public class ProductController implements IProductController {

    private final IProductService service;

    public ProductController(IProductService service) {
        this.service = service;
    }

    @Override
    public ResponseEntity<Product> insert(@Valid @RequestBody ProductRequest request){
        Product response = this.service.create(request);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.getId()).toUri();
        return  ResponseEntity.created(uri).body(response);
    }

    @Override
    public void update(@Valid @RequestBody ProductRequest request, @PathVariable Long id){
        this.service.update(id, request);
    }

    @Override
    public void delete(@PathVariable Long id) {
        this.service.delete(id);
    }

    @Override
    public Page<ProductResponse> findAll(Pageable pageable){
        return this.service.findAllPaged(pageable);
    }

    @Override
    public Product findById(@PathVariable Long id){
        return this.service.findById(id);
    }

}
