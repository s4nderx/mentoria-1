package com.dextra.mentoria.products.tests;

import com.dextra.mentoria.products.dtos.request.CategoryRequest;
import com.dextra.mentoria.products.dtos.request.ProductRequest;
import com.dextra.mentoria.products.dtos.response.CategoryResponse;
import com.dextra.mentoria.products.dtos.response.ProductResponse;
import com.dextra.mentoria.products.entities.Category;
import com.dextra.mentoria.products.entities.Product;

import java.math.BigDecimal;

public class Factory {

    public static Product createProduct() {
        Product product = new Product(1L, "Whisky Jack Daniels Honey", new BigDecimal("1500.00"));
        product.addCategory(createCategory());
        return product;
    }

    public static ProductRequest createProductRequest() {
        ProductRequest productRequest = new ProductRequest("Whisky Jack Daniels Honey", new BigDecimal("1500.00"));
        productRequest.addCategory(createCategoryResponse());
        return productRequest;
    }

    public static ProductResponse createProductResponse() {
        ProductResponse productResponse = new ProductResponse(1L, "Whisky Jack Daniels Honey", new BigDecimal("1500.00"));
        productResponse.addCategory(createCategoryResponse());
        return productResponse;
    }


    public static Category createCategory(){
        return new Category(1L, "Bebidas");
    }


    public static CategoryRequest createCategoryRequest() {
        return new CategoryRequest("Bebidas");
    }

    public static CategoryResponse createCategoryResponse() {
        return new CategoryResponse(1L, "Bebidas");
    }


}
