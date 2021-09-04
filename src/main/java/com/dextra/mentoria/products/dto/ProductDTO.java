package com.dextra.mentoria.products.dto;

import com.dextra.mentoria.products.entities.Product;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class ProductDTO {

    private Long id;
    private String name;
    private BigDecimal price;
    private final Set<CategoryDTO> categories = new HashSet<>();

    public ProductDTO(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public ProductDTO(Product entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.price = entity.getPrice();
        entity.getCategories().forEach(cat -> {
            this.categories.add(new CategoryDTO(cat));
        });

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void addCategory(CategoryDTO category) {
        this.categories.add(category);
    }

    public Set<CategoryDTO> getCategories() {
        return categories;
    }
}
