package com.dextra.mentoria.products.controllers;

import com.dextra.mentoria.products.controllers.serialization.ProductSerialization;
import com.dextra.mentoria.products.dto.request.ProductRequest;
import com.dextra.mentoria.products.dto.response.ProductResponse;
import com.dextra.mentoria.products.entities.Product;
import com.dextra.mentoria.products.services.ProductService;
import com.dextra.mentoria.products.services.exceptions.NotFoundException;
import com.dextra.mentoria.products.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ModelMapper modelMapper;

    private ProductRequest productRequest;

    private PageImpl<ProductResponse> page;
    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    void setUp() throws IOException {
        this.existingId = 1L;
        this.nonExistingId = 2L;
        this.productRequest = Factory.createProductRequest();
        ProductResponse productResponse = Factory.createProductResponse();
        Product product = Factory.createProduct();
        this.page = new PageImpl<>(List.of(productResponse));
        when(service.findAllPaged(any())).thenReturn(page);
        when(service.findById(this.existingId)).thenReturn(product);
        when(service.findById(this.nonExistingId)).thenThrow(NotFoundException.class);

        doNothing().when(service).update(eq(this.existingId), any());
        doThrow(NotFoundException.class).when(service).update(eq(this.nonExistingId), any());

        when(service.create(any())).thenReturn(product);

        doNothing().when(service).delete(this.existingId);
        doThrow(NotFoundException.class).when(service).delete(this.nonExistingId);

        when(modelMapper.map(any(), eq(Product.class))).thenReturn(Factory.createProduct());
        when(modelMapper.map(any(), eq(ProductResponse.class))).thenReturn(Factory.createProductResponse());

    }

    @Test
    public void createShouldReturnProductDtoWhenIdExists() throws Exception {
        String body = objectMapper.writeValueAsString(this.productRequest);

        ResultActions result = this.mockMvc.perform(
                post("/products")
                        .content(body)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.id").isNotEmpty());
        result.andExpect(jsonPath("$.id").isNumber());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.price").exists());
        result.andExpect(jsonPath("$.categories").exists());
        result.andExpect(status().isCreated());
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExist() throws Exception {
        String body = objectMapper.writeValueAsString(this.productRequest);
        ResultActions result = this.mockMvc.perform(
                delete("/products/{id}", this.existingId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShoulReturnNotfoundWhenIdDoesNotExist() throws Exception {
        String body = objectMapper.writeValueAsString(this.productRequest);
        ResultActions result = this.mockMvc.perform(
                delete("/products/{id}", this.nonExistingId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound());
    }

    @Test
    public void updateSouldReturnProductDtoWhenIdExists() throws Exception {

        String body = objectMapper.writeValueAsString(this.productRequest);

        ResultActions result = this.mockMvc.perform(
            put("/products/{id}", this.existingId)
                .content(body)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNoContent());
    }

    @Test
    public void updateSouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        String body = objectMapper.writeValueAsString(this.productRequest);

        ResultActions result = this.mockMvc.perform(
                put("/products/{id}", this.nonExistingId)
                        .content(body)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound());
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        String expectedResponseBody = objectMapper.writeValueAsString(this.page);
        ResultActions result = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(content().json(expectedResponseBody));
    }

    @Test
    public void findByIdShouldReturnProductWhenIdExists() throws Exception {
        ResultActions result = this.mockMvc.perform(get("/products/{id}", this.existingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.price").exists());
        result.andExpect(jsonPath("$.categories").exists());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        ResultActions result = this.mockMvc.perform(get("/products/{id}", this.nonExistingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }

}
