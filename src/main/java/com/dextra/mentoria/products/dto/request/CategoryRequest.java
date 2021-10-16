package com.dextra.mentoria.products.dto.request;

import com.dextra.mentoria.products.entities.Category;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.util.Objects;

public class CategoryRequest {

    @NotEmpty(message = "Preenchimento obrigatório")
    @Length(min = 3, max = 80, message = "O tamanho deve ser entre 3 e 80 caracteres")
    private String name;

    @Deprecated
    public CategoryRequest() {
    }

    public CategoryRequest(String name) {
        this.name = name;
    }

    public CategoryRequest(Category entity) {
        this.name = entity.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryRequest that = (CategoryRequest) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "CategoryRequest{" +
                ", name='" + name + '\'' +
                '}';
    }

}
