package com.dextra.mentoria.products.services;

import com.dextra.mentoria.products.dto.request.ProductRequest;
import com.dextra.mentoria.products.dto.response.ProductResponse;
import com.dextra.mentoria.products.entities.Product;
import com.github.fge.jsonpatch.JsonPatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IProductService {

    Product create(ProductRequest request);
    void update(Long id, ProductRequest request);
    void delete(Long id);
    Product findById(Long id);
    Page<ProductResponse> findAllPaged(Pageable pageable);

    Product patchUpdate(Long id, JsonPatch patch);
}
