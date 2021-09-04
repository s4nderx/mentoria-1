package com.dextra.mentoria.products.tests;

import com.dextra.mentoria.products.dto.CategoryDTO;
import com.dextra.mentoria.products.dto.ProductDTO;
import com.dextra.mentoria.products.entities.Category;
import com.dextra.mentoria.products.entities.Product;

import java.math.BigDecimal;

public class Factory {

    public static Product createProduct() {
        Product product = new Product(1L, "Cadeira Gamer", new BigDecimal("1500.00"));
        product.addCategory(new Category(2L, "Casa"));
        return product;
    }

    public static ProductDTO createProductDTO() {
        return new ProductDTO(createProduct());
    }

    public static Category createCategory(){
        return new Category(1L, "Bebidas");
    }


    public static CategoryDTO createCategoryDTO(){
        return new CategoryDTO(createCategory());
    }

}
