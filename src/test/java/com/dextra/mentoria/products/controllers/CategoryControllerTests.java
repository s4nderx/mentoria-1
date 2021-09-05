package com.dextra.mentoria.products.controllers;

import com.dextra.mentoria.products.dto.CategoryDTO;
import com.dextra.mentoria.products.services.CategoryServiceImpl;
import com.dextra.mentoria.products.services.exceptions.DataIntegrityException;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
public class CategoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryServiceImpl service;

    @Autowired
    private ObjectMapper objectMapper;

    private CategoryDTO categoryDTO;
    private PageImpl<CategoryDTO> page;
    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;

    @BeforeEach
    void setUp() {
        this.existingId = 1L;
        this.nonExistingId = 2L;
        this.dependentId  = 3L;
        this.categoryDTO = Factory.createCategoryDTO();
        this.page = new PageImpl<>(List.of(this.categoryDTO));
        when(service.findAllPaged(any())).thenReturn(page);
        when(service.findById(this.existingId)).thenReturn(this.categoryDTO);
        when(service.findById(this.nonExistingId)).thenThrow(NotFoundException.class);

        when(service.update(eq(this.existingId), any())).thenReturn(this.categoryDTO);
        when(service.update(eq(this.nonExistingId), any())).thenThrow(NotFoundException.class);

        when(service.create(any())).thenReturn(this.categoryDTO);

        doNothing().when(service).delete(this.existingId);
        doThrow(NotFoundException.class).when(service).delete(this.nonExistingId);
        doThrow(DataIntegrityException.class).when(service).delete(this.dependentId);
    }

    @Test
    public void createShouldReturnCategoryDtoWhenIdExists() throws Exception {
        CategoryDTO dto = Factory.createCategoryDTO();
        dto.setId(null);
        String body = objectMapper.writeValueAsString(dto);

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
    public void deleteShouldReturnNoContentWhenIdExist() throws Exception {
        String body = objectMapper.writeValueAsString(this.categoryDTO);
        ResultActions result = this.mockMvc.perform(
                delete("/categories/{id}", this.existingId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShoulReturnNotfoundWhenIdDoesNotExist() throws Exception {
        String body = objectMapper.writeValueAsString(this.categoryDTO);
        ResultActions result = this.mockMvc.perform(
                delete("/categories/{id}", this.nonExistingId)
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isNotFound());
    }

    @Test
    public void updateSouldReturnCategoryDtoWhenIdExists() throws Exception {

        String body = objectMapper.writeValueAsString(this.categoryDTO);

        ResultActions result = this.mockMvc.perform(
            put("/categories/{id}", this.existingId)
                .content(body)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
    }

    @Test
    public void updateSouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        String body = objectMapper.writeValueAsString(this.categoryDTO);

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
