package com.dextra.mentoria.products.controllers;

import com.dextra.mentoria.products.dto.request.ProductRequest;
import com.dextra.mentoria.products.dto.response.ProductResponse;
import com.dextra.mentoria.products.services.ProductService;
import com.dextra.mentoria.products.services.exceptions.NotFoundException;
import com.dextra.mentoria.products.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
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

    private ProductRequest productRequest;
    private ProductResponse productResponse;

    private PageImpl<ProductResponse> page;
    private Long existingId;
    private Long nonExistingId;

    @BeforeEach
    void setUp() {
        this.existingId = 1L;
        this.nonExistingId = 2L;
        this.productRequest = Factory.createProductRequest();
        this.productResponse = Factory.createProductResponse();
        this.page = new PageImpl<>(List.of(this.productResponse));
        when(service.findAllPaged(any())).thenReturn(page);
        when(service.findById(this.existingId)).thenReturn(this.productResponse);
        when(service.findById(this.nonExistingId)).thenThrow(NotFoundException.class);

        when(service.update(eq(this.existingId), any())).thenReturn(this.productResponse);
        when(service.update(eq(this.nonExistingId), any())).thenThrow(NotFoundException.class);

        when(service.create(any())).thenReturn(this.productResponse);

        doNothing().when(service).delete(this.existingId);
        doThrow(NotFoundException.class).when(service).delete(this.nonExistingId);
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

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.price").exists());
        result.andExpect(jsonPath("$.categories").exists());
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
