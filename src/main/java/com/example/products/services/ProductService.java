package com.example.products.services;

import com.example.products.dto.ProductDTO;
import com.example.products.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ProductService {

    ProductDTO create(ProductDTO dto);
    ProductDTO update(Long id, ProductDTO dto);
    void delete(Long id);
    ProductDTO findById(Long id);
    Page<ProductDTO> findAllPaged(PageRequest pageRequest);
    Product find(Long id);
}
