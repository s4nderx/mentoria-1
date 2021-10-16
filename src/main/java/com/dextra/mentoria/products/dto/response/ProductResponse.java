package com.dextra.mentoria.products.dto.response;

import com.dextra.mentoria.products.dto.CategoryDTO;
import com.dextra.mentoria.products.entities.Product;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ProductResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Set<CategoryDTO> categories = new HashSet<>();

    @Deprecated
    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
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

    @Deprecated
    public void setCategories(Set<CategoryDTO> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductResponse dto = (ProductResponse) o;
        return id.equals(dto.id) && Objects.equals(name, dto.name) && Objects.equals(price, dto.price) && Objects.equals(categories, dto.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, categories);
    }
}
