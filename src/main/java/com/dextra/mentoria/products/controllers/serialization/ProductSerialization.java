package com.dextra.mentoria.products.controllers.serialization;

import com.dextra.mentoria.products.dto.response.ProductResponse;
import com.dextra.mentoria.products.entities.Product;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.modelmapper.ModelMapper;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class ProductSerialization extends JsonSerializer<Product> {

    private final ModelMapper modelMapper;

    public ProductSerialization(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public void serialize(Product product, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        ProductResponse productResponse = modelMapper.map(product, ProductResponse.class);
        jsonGenerator.writeObject(productResponse);
    }

}
