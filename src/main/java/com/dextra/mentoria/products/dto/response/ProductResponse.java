package com.dextra.mentoria.products.dto.response;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ProductResponse {

    private Long id;
    private String name;
    private BigDecimal price;
    private Set<CategoryResponse> categories = new HashSet<CategoryResponse>();

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
        ProductResponse dto = (ProductResponse) o;
        return id.equals(dto.id) && Objects.equals(name, dto.name) && Objects.equals(price, dto.price) && Objects.equals(categories, dto.categories);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, categories);
    }
}
