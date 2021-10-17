package com.dextra.mentoria.products.config;

import com.dextra.mentoria.products.dtos.request.ProductRequest;
import com.dextra.mentoria.products.dtos.response.ProductResponse;
import com.dextra.mentoria.products.entities.Product;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration().isCollectionsMergeEnabled();

        modelMapper.createTypeMap(Product.class, ProductResponse.class)
                .addMapping(Product::getCategories, ProductResponse::setCategories);

        modelMapper.createTypeMap(ProductRequest.class, Product.class)
                .addMapping(ProductRequest::getCategories, Product::setCategories);

        return modelMapper;
    }
}
