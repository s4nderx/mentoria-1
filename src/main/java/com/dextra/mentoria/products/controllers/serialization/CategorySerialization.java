package com.dextra.mentoria.products.controllers.serialization;

import com.dextra.mentoria.products.dto.response.CategoryResponse;
import com.dextra.mentoria.products.entities.Category;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.modelmapper.ModelMapper;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class CategorySerialization extends JsonSerializer<Category> {

    private final ModelMapper modelMapper;

    public CategorySerialization(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public void serialize(Category category, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        CategoryResponse categoryResponse = modelMapper.map(category, CategoryResponse.class);
        jsonGenerator.writeObject(categoryResponse);
    }
}
