package com.dextra.mentoria.products.services;

import com.dextra.mentoria.products.dtos.response.CategoryResponse;
import com.dextra.mentoria.products.entities.Category;
import com.dextra.mentoria.products.repositories.CategoryRepository;
import com.dextra.mentoria.products.services.exceptions.DataIntegrityException;
import com.dextra.mentoria.products.services.exceptions.NotFoundException;
import com.dextra.mentoria.products.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class ICategoryServiceTests {

    @InjectMocks
    private CategoryService service;

    @Mock
    private CategoryRepository repository;

    @Mock
    private ModelMapper modelMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;

    @BeforeEach
    void setUp() {
        this.existingId = 1L;
        this.nonExistingId = Long.MAX_VALUE;
        this.dependentId = 5L;
        Category category = Factory.createCategory();
        PageImpl<Category> page = new PageImpl<>(List.of(category));

        when(repository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);
        when(repository.save(any())).thenReturn(category);
        when(repository.findById(this.existingId)).thenReturn(Optional.of(category));
        when(repository.findById(this.nonExistingId)).thenReturn(Optional.empty());
        doNothing().when(repository).deleteById(this.existingId);
        doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(this.nonExistingId);
        doThrow(DataIntegrityViolationException.class).when(repository).deleteById(this.dependentId);

        when(modelMapper.map(any(), eq(Category.class))).thenReturn(Factory.createCategory());
        when(modelMapper.map(any(), eq(CategoryResponse.class))).thenReturn(Factory.createCategoryResponse());

    }

    @Test
    public void findAllPagedShouldReturnPage(){
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> res = service.findAllPaged(pageable);
        assertNotNull(res);
        verify(repository, times(1)).findAll(pageable);
    }

    @Test
    public void deleteShouldDoNothingWhenIdExist(){
        assertDoesNotThrow(() -> this.service.delete(this.existingId));

        verify(repository, times(1)).deleteById(this.existingId);
    }

    @Test
    public void deleteShouldThrowNotFoundExceptionWhenIdDoesNotExist(){
        assertThrows(NotFoundException.class, () -> this.service.delete(this.nonExistingId));

        verify(repository, times(1)).deleteById(this.nonExistingId);
    }

    @Test
    public void deleteShouldThrowDataIntegrityExceptionWhenIdIsDependent(){

        assertThrows(DataIntegrityException.class, () -> this.service.delete(this.dependentId));

        verify(repository, times(1)).deleteById(this.dependentId);
    }

    @Test
    public void findByIdShouldReturnAnCategoryDtoWhenIdExist() {
        Category category = this.service.findById(this.existingId);
        assertNotNull(category);
        verify(repository, times(1)).findById(this.existingId);
    }

    @Test
    public void findByIdShouldThrowNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(NotFoundException.class, () -> this.service.findById(this.nonExistingId));
        verify(repository, times(1)).findById(this.nonExistingId);
    }

    @Test
    public void createShouldReturnAnNewCategoryWithIdWhenIdIsNull() {
        Category category = this.service.create(Factory.createCategoryRequest());
        assertNotNull(category.getId());
        verify(this.repository, times(1)).save(any());
    }

    @Test
    public void updateShouldReturnAnCategoryWhenIdExist() {
        this.service.update(this.existingId, Factory.createCategoryRequest());
        verify(this.repository, times(1)).save(any());
    }

    @Test
    public void updateShouldThrowNotFoundExceptionWhenIdDoesNotExist() {
        assertThrows(NotFoundException.class, () -> this.service.update(this.nonExistingId, Factory.createCategoryRequest()));
    }

}
