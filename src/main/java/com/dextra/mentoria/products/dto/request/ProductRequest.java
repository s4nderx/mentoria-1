package com.dextra.mentoria.products.dto.request;

import com.dextra.mentoria.products.dto.response.CategoryResponse;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ProductRequest {

    private String name;
    private BigDecimal price;
    private Set<CategoryResponse> categories = new HashSet<CategoryResponse>();

    @Deprecated
    public void ProductRequest() {
    }

    public ProductRequest(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
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

    public void addCategory(CategoryResponse category) {
        this.categories.add(category);
    }

    public Set<CategoryResponse> getCategories() {
        return categories;
    }

    @Deprecated
    public void setCategories(Set<CategoryResponse> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductRequest dto = (ProductRequest) o;
        return Objects.equals(name, dto.name) && Objects.equals(price, dto.price) && Objects.equals(categories, dto.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, categories);
    }

}
