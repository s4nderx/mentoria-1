package com.dextra.mentoria.products.services;

import com.dextra.mentoria.products.dto.ProductDTO;
import com.dextra.mentoria.products.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    ProductDTO create(ProductDTO dto);
    ProductDTO update(Long id, ProductDTO dto);
    void delete(Long id);
    ProductDTO findById(Long id);
    Page<ProductDTO> findAllPaged(Pageable pageable);
    Product find(Long id);
}
