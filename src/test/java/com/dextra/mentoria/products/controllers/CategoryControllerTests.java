package com.dextra.mentoria.products.controllers;

import com.dextra.mentoria.products.dtos.response.CategoryResponse;
import com.dextra.mentoria.products.entities.Category;
import com.dextra.mentoria.products.services.CategoryService;
import com.dextra.mentoria.products.services.exceptions.DataIntegrityException;
import com.dextra.mentoria.products.services.exceptions.NotFoundException;
import com.dextra.mentoria.products.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService service;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ModelMapper modelMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;

    @BeforeEach
    void setUp() {
        this.existingId = 1L;
        this.nonExistingId = 2L;
        this.dependentId  = 3L;
        CategoryResponse categoryResponse = Factory.createCategoryResponse();
        Category category = Factory.createCategory();

        PageImpl<Category> page = new PageImpl<>(List.of(category));

        when(service.findAllPaged(any())).thenReturn(page);
        when(service.findById(this.existingId)).thenReturn(category);
        when(service.findById(this.nonExistingId)).thenThrow(NotFoundException.class);
        when(service.create(any())).thenReturn(category);

        doNothing().when(service).delete(this.existingId);
        doNothing().when(service).update(eq(this.existingId), any());

        doThrow(NotFoundException.class).when(service).delete(this.nonExistingId);
        doThrow(DataIntegrityException.class).when(service).delete(this.dependentId);
        doThrow(NotFoundException.class).when(service).update(eq(this.nonExistingId), any());

        when(modelMapper.map(any(), eq(Category.class))).thenReturn(Factory.createCategory());
        when(modelMapper.map(any(), eq(CategoryResponse.class))).thenReturn(Factory.createCategoryResponse());

    }

    @Test
    public void createShouldReturnCategoryDtoWhenIdExists() throws Exception {
        String body = objectMapper.writeValueAsString(Factory.createProductRequest());

        ResultActions result = this.mockMvc.perform(
                post("/categories")
                        .content(body)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.id").isNotEmpty());
        result.andExpect(jsonPath("$.id").isNumber());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(status().isCreated());
    }

    @Test
    public void deleteShouldReturnBadRequestWhenIsDependentId() throws Exception {
        String body = objectMapper.writeValueAsString(Factory.createProductRequest());
        ResultActions result = this.mockMvc.perform(
                delete("/categories/{id}", this.dependentId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isBadRequest());
    }

    @Test
    public void deleteShouldReturnNoContentWhenIdExist() throws Exception {
        String body = objectMapper.writeValueAsString(Factory.createProductRequest());
        ResultActions result = this.mockMvc.perform(
                delete("/categories/{id}", this.existingId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShoulReturnNotfoundWhenIdDoesNotExist() throws Exception {
        String body = objectMapper.writeValueAsString(Factory.createProductRequest());
        ResultActions result = this.mockMvc.perform(
                delete("/categories/{id}", this.nonExistingId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound());
    }

    @Test
    public void updateShouldReturnCategoryDtoWhenIdExists() throws Exception {

        String body = objectMapper.writeValueAsString(Factory.createProductRequest());

        ResultActions result = this.mockMvc.perform(
            put("/categories/{id}", this.existingId)
                .content(body)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNoContent());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        String body = objectMapper.writeValueAsString(Factory.createProductRequest());

        ResultActions result = this.mockMvc.perform(
                put("/categories/{id}", this.nonExistingId)
                        .content(body)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound());
    }

    @Test
    public void findAllShouldReturnPage() throws Exception {
        ResultActions result = mockMvc.perform(get("/categories").accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
    }

    @Test
    public void findByIdShouldReturnCategoryWhenIdExists() throws Exception {
        ResultActions result = this.mockMvc.perform(get("/categories/{id}", this.existingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
        ResultActions result = this.mockMvc.perform(get("/categories/{id}", this.nonExistingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }

}
